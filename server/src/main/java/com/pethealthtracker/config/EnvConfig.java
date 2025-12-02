package com.pethealthtracker.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@Profile("!test")
public class EnvConfig implements EnvironmentAware, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(EnvConfig.class);
    private ConfigurableEnvironment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
    }

    @Override
    public void afterPropertiesSet() {
        loadEnv();
    }

    public void loadEnv() {
        try {
            String projectDir = System.getProperty("user.dir");
            String envPath = Paths.get(projectDir, ".env").toString();

            logger.info("Buscando archivo .env en: {}", envPath);

            if (!Files.exists(Paths.get(envPath))) {
                logger.warn("El archivo .env no existe en: {}", envPath);
                return;
            }

            // Leer el contenido y remover BOM si está presente
            String content = new String(Files.readAllBytes(Paths.get(envPath)), StandardCharsets.UTF_8);
            content = content.replace("\uFEFF", ""); // Eliminar BOM si existe

            // Crear un archivo temporal con el contenido limpio
            Path tempFile = Files.createTempFile("temp-env", ".env");
            Files.write(tempFile, content.getBytes(StandardCharsets.UTF_8));

            try {
                // Cargar desde el archivo temporal limpio
                Dotenv dotenv = Dotenv.configure()
                        .directory(tempFile.getParent().toString())
                        .filename(tempFile.getFileName().toString())
                        .load();

                Properties envProperties = new Properties();

                // Lista de variables obligatorias
                String[] requiredVars = {
                        "DB_USERNAME", "DB_PASSWORD", "DB_HOST",
                        "DB_PORT", "DB_DATABASE", "JWT_SECRET",
                        "JWT_EXPIRATION", "EMAIL_HOST", "EMAIL_PORT",
                        "EMAIL_USERNAME", "EMAIL_PASSWORD"
                };

                // Cargar todas las variables
                for (String key : requiredVars) {
                    String value = dotenv.get(key);
                    if (value == null || value.trim().isEmpty()) {
                        logger.warn("ADVERTENCIA: La variable '{}' no está definida o está vacía", key);
                    } else {
                        // Agregar a las propiedades del sistema si no están ya establecidas
                        if (System.getProperty(key) == null) {
                            System.setProperty(key, value.trim());
                        }
                        // Agregar a nuestro objeto de propiedades
                        envProperties.setProperty(key, value.trim());

                        // Registrar la variable (enmascarar datos sensibles)
                        boolean isSensitive = key.toUpperCase().contains("PASS") ||
                                key.toUpperCase().contains("SECRET") ||
                                key.toUpperCase().contains("KEY") ||
                                key.toUpperCase().contains("PASSWORD");
                        logger.info("Cargada variable: {}={}", key, isSensitive ? "****" : value);
                    }
                }

                // Cargar variables opcionales
                String[] optionalVars = {
                        "PORT", "GOOGLE_CLIENT_ID", "GOOGLE_CLIENT_SECRET",
                        "GOOGLE_CLIENT_ID_ANDROID", "API_URL", "GOOGLE_PASSWORD_TEMP"
                };

                for (String key : optionalVars) {
                    String value = dotenv.get(key);
                    if (value != null && !value.trim().isEmpty()) {
                        if (System.getProperty(key) == null) {
                            System.setProperty(key, value.trim());
                        }
                        envProperties.setProperty(key, value.trim());

                        boolean isSensitive = key.toUpperCase().contains("SECRET") ||
                                key.toUpperCase().contains("PASSWORD");
                        logger.debug("Cargada variable opcional: {}={}",
                                key, isSensitive ? "****" : value);
                    }
                }

                if (envProperties.isEmpty()) {
                    logger.warn("No se encontraron variables en el archivo .env o el archivo está vacío");
                    return;
                }

                // Agregar propiedades al entorno de Spring con la mayor prioridad
                environment.getPropertySources()
                        .addFirst(new PropertiesPropertySource("dotenvProperties", envProperties));

                logger.info("Se cargaron {} variables de entorno desde .env", envProperties.size());

            } finally {
                // Limpiar el archivo temporal
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException e) {
                    logger.warn("No se pudo eliminar el archivo temporal: {}", e.getMessage());
                }
            }

        } catch (Exception e) {
            logger.error("Error al cargar las variables de entorno: {}", e.getMessage(), e);
            throw new RuntimeException("Error al cargar las variables de entorno", e);
        }
    }
}