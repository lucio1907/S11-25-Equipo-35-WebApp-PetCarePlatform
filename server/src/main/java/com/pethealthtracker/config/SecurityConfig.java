package com.pethealthtracker.config;

import com.pethealthtracker.model.User;
import com.pethealthtracker.model.enums.Role;
import com.pethealthtracker.repository.UserRepository;
import com.pethealthtracker.security.JwtAuthenticationFilter;
import com.pethealthtracker.security.JwtTokenProvider;

import com.pethealthtracker.security.UserPrincipal;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

import java.util.Arrays;

import static com.pethealthtracker.model.enums.Role.ROLE_USER;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final JwtAuthenticationFilter jwtAuthFilter;

    @Value("${app.oauth.default-password}")
    private String oauthDefaultPassword;

    // Lista blanca de rutas que no requieren autenticación
    private static final String[] WHITE_LIST_URL = {
            // Auth endpoints
            "/auth/**",
            "/api/auth/**",

            // API Documentation - Rutas principales de Swagger UI y OpenAPI
            "/v3/api-docs/**",
            "/v3/api-docs",
            "/v3/api-docs.yaml",
            "/v3/api-docs/swagger-config",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-ui.html/**",
            "/swagger-ui/index.html",
            "/swagger-resources/**",
            "/swagger-resources",
            "/webjars/**",
            "/webjars/springdoc-openapi-ui/**",
            "/favicon.ico",
            "/error",

            // H2 Console (solo para desarrollo)
            "/h2-console/**"
    };
    
    private static final String[] ADMIN_URLS = {
        "/api/admin/**"
    };
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Add your frontend URL
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configuración de CORS
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        
        // Deshabilitar CSRF
        http.csrf(csrf -> csrf.disable());
        
        // Configuración de sesión sin estado
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        
        // Configuración de autorización
        http.authorizeHttpRequests(authorize -> {
            // Permitir todas las solicitudes OPTIONS
            authorize.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
            
            // Permitir acceso a las rutas en la lista blanca
            Arrays.stream(WHITE_LIST_URL).forEach(pattern -> 
                authorize.requestMatchers(pattern).permitAll()
            );
            
            // Configurar acceso a rutas de administración
            authorize.requestMatchers("/api/admin/users").permitAll(); // Temporalmente permitido para crear el primer admin
            
            // El resto de rutas de administración requieren rol ADMIN
            authorize.requestMatchers("/api/admin/**").hasRole("ADMIN");
            
            // Todas las demás solicitudes requieren autenticación
            authorize.anyRequest().authenticated();
        });
        
        // Añadir el filtro JWT
        http.authenticationProvider(authenticationProvider);
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    @Bean
    public AuthenticationSuccessHandler oauthSuccessHandler() {
        return (request, response, authentication) -> {
            if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
                String userEmail = oauthToken.getPrincipal().getAttribute("email");
                String userFirstName = oauthToken.getPrincipal().getAttribute("given_name");
                String userLastName = oauthToken.getPrincipal().getAttribute("family_name");
                // Create final copies for use in lambda
                final String finalUserFirstName = (userFirstName != null) ? userFirstName : oauthToken.getPrincipal().getAttribute("name");
                final String finalUserLastName = (userLastName != null) ? userLastName : "-";

                // Find or create user
                User user = userRepository.findByEmail(userEmail).orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(userEmail);
                    newUser.setFirstName(finalUserFirstName);
                    newUser.setLastName(finalUserLastName);
                    newUser.addRole(Role.ROLE_USER);
                    newUser.setEmailVerified(true);
                    return userRepository.save(newUser);
                });

                // Create an Authentication object for the user
                UserPrincipal userPrincipal = UserPrincipal.create(user);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userPrincipal, 
                    null, 
                    userPrincipal.getAuthorities()
                );
                
                // Generate JWT token with the authentication object
                String token = jwtTokenProvider.generateToken(authToken);
                
                // Get the user's role for the response
                String role = user.getRoles().stream()
                    .findFirst()
                    .map(Enum::name)
                    .orElse("USER"); // Default role if no roles

                // Configure response
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(
                    String.format(
                        "{\"token\":\"%s\",\"email\":\"%s\",\"role\":\"%s\"}",
                        token, user.getEmail(), role
                    )
                    );
                } else {
                    new SavedRequestAwareAuthenticationSuccessHandler().onAuthenticationSuccess(request, response, authentication);
                }
            };
        }
    }
