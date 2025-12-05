package com.pethealthtracker.controller;

import com.pethealthtracker.service.cloudinary.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/images")
@Tag(name = "Gestión de Imágenes", description = "API para la gestión de imágenes en Cloudinary")
public class ImageController {

    private final CloudinaryService cloudinaryService;

    public ImageController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/upload")
    @Operation(summary = "Subir una imagen",
            description = "Sube una imagen a Cloudinary y devuelve la información de la imagen subida",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Imagen subida exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Error al subir la imagen")
            })
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Validar que el archivo no esté vacío
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("El archivo no puede estar vacío");
            }

            // Validar el tipo de archivo
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body("Solo se permiten archivos de imagen");
            }

            // Subir el archivo a Cloudinary
            Map<String, String> uploadResult = cloudinaryService.uploadFile(file);

            // Construir la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Imagen subida exitosamente");
            response.put("data", uploadResult);

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error al subir la imagen: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{publicId}")
    @Operation(summary = "Eliminar una imagen",
            description = "Elimina una imagen de Cloudinary usando su ID público",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Imagen eliminada exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Error al eliminar la imagen")
            })
    public ResponseEntity<?> deleteImage(@PathVariable String publicId) {
        try {
            cloudinaryService.deleteFile(publicId);
            return ResponseEntity.ok().body("Imagen eliminada exitosamente");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error al eliminar la imagen: " + e.getMessage());
        }
    }

    @GetMapping("/url/{publicId}")
    @Operation(summary = "Obtener URL de imagen",
            description = "Obtiene la URL de una imagen con opciones de redimensionamiento",
            responses = {
                    @ApiResponse(responseCode = "200", description = "URL de la imagen generada exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Error al generar la URL de la imagen")
            })
    public ResponseEntity<?> getImageUrl(
            @PathVariable String publicId,
            @RequestParam(required = false) Integer width,
            @RequestParam(required = false) Integer height) {

        try {
            String imageUrl = cloudinaryService.getImageUrl(publicId, width, height);

            Map<String, Object> response = new HashMap<>();
            response.put("publicId", publicId);
            response.put("url", imageUrl);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al generar la URL de la imagen: " + e.getMessage());
        }
    }
}