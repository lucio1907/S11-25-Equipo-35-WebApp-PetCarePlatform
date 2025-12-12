package com.pethealthtracker.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Configuration
public class EnvConfig {
    private static final Logger logger = Logger.getLogger(EnvConfig.class.getName());

    @PostConstruct
    public void loadEnv() {
        try {
            logger.info("Iniciando carga de variables de entorno...");

            // Obtener la ruta del directorio del proyecto
            String projectPath = System.getProperty("user.dir");
            logger.info("Buscando .env en: " + projectPath);

            // Configurar Dotenv
            Dotenv dotenv;
            try {
                dotenv = Dotenv.configure()
                        .directory(projectPath)
                        .filename(".env")
                        .load();
            } catch (Exception e) {
                logger.severe("No se pudo cargar el archivo .env: " + e.getMessage());
                throw new RuntimeException("El archivo .env es requerido", e);
            }

            // Cargar variables
            dotenv.entries().forEach(entry -> {
                String key = entry.getKey();
                String value = entry.getValue();
                if (System.getProperty(key) == null) {
                    System.setProperty(key, value);
                    logger.info("Variable cargada: " + key);
                }
            });

            logger.info("Variables de entorno cargadas exitosamente");

        } catch (Exception e) {
            logger.severe("Error al cargar las variables de entorno: " + e.getMessage());
            throw new RuntimeException("Error crítico en la configuración", e);
        }
    }
}