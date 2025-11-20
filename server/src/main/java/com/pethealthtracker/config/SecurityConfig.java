package com.pethealthtracker.config;

import com.pethealthtracker.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    // Lista blanca de rutas que no requieren autenticación
    private static final String[] WHITE_LIST_URL = {
        // Auth endpoints
        "/auth/**",
            // "/api/**",

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

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configuración de CORS
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        
        // Deshabilitar CSRF para todas las solicitudes
        http.csrf(csrf -> csrf.disable());
        
        // Configuración de autorización de solicitudes
        http.authorizeHttpRequests(auth -> {
            // Permitir todas las solicitudes OPTIONS
            auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
            
            // Permitir acceso a las rutas en la lista blanca
            auth.requestMatchers(WHITE_LIST_URL).permitAll();
            
            // Permitir el acceso a la documentación de la API
            auth.requestMatchers(
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html"
            ).permitAll();
            
            // Todas las demás solicitudes requieren autenticación
            auth.anyRequest().authenticated();
        });
        
        // Configuración de la gestión de sesión
        http.sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        
        // Añadir el filtro JWT antes del filtro de autenticación de nombre de usuario y contraseña
        http.authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        // Configuración de cabeceras para desarrollo
        http.headers(headers -> {
            // Deshabilitar la protección X-Frame-Options para H2 Console
            headers.frameOptions(frame -> frame.disable());
            
            // Configuración de caché
            headers.cacheControl(cache -> {});
            
            // Configuración de seguridad de contenido
            headers.contentSecurityPolicy(csp -> csp.policyDirectives(
                "default-src 'self'; " +
                "script-src 'self' 'unsafe-inline' 'unsafe-eval'; " +
                "style-src 'self' 'unsafe-inline'; " +
                "img-src 'self' data:; " +
                "font-src 'self'"
            ));
        });

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Permitir desde cualquier origen en desarrollo
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",
            "http://127.0.0.1:3000",
            "http://localhost:5000",
            "http://127.0.0.1:5000"
        ));
        
        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"
        ));
        
        // Cabeceras permitidas
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "Accept",
            "X-Requested-With",
            "Cache-Control",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));
        
        // Cabeceras expuestas
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "Content-Disposition",
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials"
        ));
        
        // Tiempo máximo de caché de la configuración CORS (en segundos)
        configuration.setMaxAge(3600L);
        
        // Permitir credenciales
        configuration.setAllowCredentials(true);
        
        // Configuración para todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
