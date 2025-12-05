package com.pethealthtracker.config;

import com.cloudinary.Cloudinary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@DependsOn("envConfig")
public class CloudinaryConfig {
    private static final Logger logger = LoggerFactory.getLogger(CloudinaryConfig.class);

    @Value("${CLOUDINARY_URL:}")
    private String cloudinaryUrl;

    @Bean
    public Cloudinary cloudinary() {
        if (cloudinaryUrl == null || cloudinaryUrl.isEmpty()) {
            // Intentar obtener de las variables de entorno del sistema
            cloudinaryUrl = System.getenv("CLOUDINARY_URL");

            if (cloudinaryUrl == null || cloudinaryUrl.isEmpty()) {
                logger.warn("CLOUDINARY_URL no está definida. Los servicios de Cloudinary no estarán disponibles.");
                return null;
            }
        }

        try {
            logger.info("Configurando Cloudinary con URL proporcionada");
            return new Cloudinary(cloudinaryUrl);
        } catch (Exception e) {
            logger.error("Error configurando Cloudinary: {}", e.getMessage(), e);
            return null;
        }
    }
}