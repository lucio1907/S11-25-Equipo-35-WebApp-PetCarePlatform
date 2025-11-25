package com.pethealthtracker.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.logging.Logger;

@Configuration
public class EnvConfig {

    private static final Logger logger = Logger.getLogger(EnvConfig.class.getName());

    @PostConstruct
    public void loadEnv() {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .directory("./")
                    .ignoreIfMissing()
                    .load();

            // Set system properties from .env
            dotenv.entries().forEach(entry -> {
                String key = entry.getKey();
                String value = entry.getValue();
                if (System.getProperty(key) == null) {
                    System.setProperty(key, value);
                    logger.info("Loaded environment variable: " + key);
                }
            });
        } catch (Exception e) {
            logger.severe("Error loading .env file: " + e.getMessage());
            throw new RuntimeException("Failed to load environment variables", e);
        }
    }
}