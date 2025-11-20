package com.pethealthtracker.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${server.servlet.context-path:/}")
    private String contextPath;

    @Bean
    OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        
        // Construir la URL base
        String serverUrl = "http://localhost:" + serverPort + 
                         (contextPath.endsWith("/") ? contextPath : contextPath + "/");
        
        return new OpenAPI()
                .servers(List.of(new Server().url(serverUrl)))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .info(new Info()
                        .title("API - Seguimiento de Salud para Mascotas ( API - Pet Health Tracker)")
                        .description("""
                            <h2>API para el sistema de seguimiento de salud de mascotas</h2>
                            <p>Esta documentación describe los endpoints disponibles en la API de Pet Health Tracker.</p>
                            <p><strong>Autenticación:</strong> Utilice el endpoint de inicio de sesión para obtener un token JWT y haga clic en el botón 'Authorize' (candado) para incluirlo en las solicitudes.</p>
                            <p><strong>Nota:</strong> Todos los endpoints requieren autenticación, excepto los de registro e inicio de sesión.</p>
                            """)
                        .version("1.0.0")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")
                        )
                );
    }
}
