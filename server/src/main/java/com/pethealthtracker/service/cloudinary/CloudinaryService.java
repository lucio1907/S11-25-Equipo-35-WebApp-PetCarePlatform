package com.pethealthtracker.service.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Map<String, String> uploadFile(MultipartFile file) throws IOException {
        try {
            // Validar que el archivo no esté vacío
            if (file.isEmpty()) {
                throw new IllegalArgumentException("El archivo no puede estar vacío");
            }

            // Subir el archivo a Cloudinary
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "auto",
                            "folder", "pet_health_tracker"
                    )
            );

            // Extraer la información relevante
            Map<String, String> result = new java.util.HashMap<>();
            result.put("public_id", (String) uploadResult.get("public_id"));
            result.put("url", (String) uploadResult.get("secure_url"));
            result.put("format", (String) uploadResult.get("format"));
            result.put("resource_type", (String) uploadResult.get("resource_type"));

            return result;
        } catch (IOException e) {
            throw new IOException("Error al subir el archivo a Cloudinary: " + e.getMessage(), e);
        }
    }

    public Map<?, ?> deleteFile(String publicId) throws IOException {
        try {
            return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new IOException("Error al eliminar el archivo de Cloudinary: " + e.getMessage(), e);
        }
    }

    public String getImageUrl(String publicId, Integer width, Integer height) {
        try {
            // Configurar las opciones de transformación
            com.cloudinary.Transformation transformation = new com.cloudinary.Transformation();

            if (width != null && height != null) {
                transformation.width(width).height(height).crop("fill");
            }

            // Construir la URL con las transformaciones
            return cloudinary.url()
                    .transformation(transformation)
                    .generate(publicId);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar la URL de la imagen: " + e.getMessage(), e);
        }
    }
}