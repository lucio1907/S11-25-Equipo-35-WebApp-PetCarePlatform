package com.pethealthtracker.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths; // <-- Importación necesaria
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer; // <-- Importación necesaria
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

                // Construye la URL base del servidor
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
                                                                                                .bearerFormat("JWT")))
                                .info(new Info()
                                                .title("API - Seguimiento de Salud para Mascotas (Pet Health Tracker)")
                                                .description("""
                                                                <h2>API para el sistema de seguimiento de salud de mascotas</h2>
                                                                <p>Esta documentación describe los endpoints disponibles en la API.</p>
                                                                <p><strong>Autenticación:</strong> Obtén el token en <code>/api/auth/login</code> y úsalo en el botón 'Authorize'.</p>
                                                                """)
                                                .version("1.0.0")
                                                .license(new License()
                                                                .name("Apache 2.0")
                                                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
        }

        @Bean
        OpenApiCustomizer prefixPathsCustomizer() {
                return openApi -> {
                        Paths oldPaths = openApi.getPaths();
                        if (oldPaths == null)
                                return; 

                        Paths newPaths = new Paths();

                        oldPaths.forEach((path, pathItem) -> {
                                String newPath = path.startsWith("/api") ? path : "/api" + path;
                                newPaths.addPathItem(newPath, pathItem);
                        });

                        // Reemplaza las rutas viejas con las nuevas modificadas
                        openApi.setPaths(newPaths);
                };
        }
}