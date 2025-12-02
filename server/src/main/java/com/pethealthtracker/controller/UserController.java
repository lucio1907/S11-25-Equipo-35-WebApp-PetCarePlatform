package com.pethealthtracker.controller;

import com.pethealthtracker.dto.ApiResponse;
import com.pethealthtracker.dto.user.UserDto;
import com.pethealthtracker.service.FileStorageService;
import com.pethealthtracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@Tag(name = "Usuarios (users)", description = "API para la gestión de usuarios")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;
    private final FileStorageService fileStorageService;

    @Operation(summary = "Obtiene el perfil del usuario actual")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getCurrentUser() {
        UserDto user = userService.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.<UserDto>builder()
                .success(true)
                .data(user)
                .build());
    }

    @Operation(summary = "Actualiza el perfil del usuario actual")
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> updateUserProfile(
            @Valid @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(userDto);
        return ResponseEntity.ok(ApiResponse.<UserDto>builder()
                .success(true)
                .message("Perfil actualizado correctamente")
                .data(updatedUser)
                .build());
    }

    @Operation(summary = "Elimina el usuario actual")
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteUser() {
        userService.deleteUser();
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Usuario eliminado correctamente")
                .build());
    }

    @Operation(summary = "Obtiene todos los usuarios (solo administradores)")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.<List<UserDto>>builder()
                .success(true)
                .data(users)
                .build());
    }

    @Operation(summary = "Obtiene un usuario por ID (solo administradores)")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.<UserDto>builder()
                .success(true)
                .data(user)
                .build());
    }

    @Operation(summary = "Cambia la contraseña del usuario actual")
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword) {
        userService.changePassword(currentPassword, newPassword);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Contraseña actualizada correctamente")
                .build());
    }

    @Operation(summary = "Sube una imagen de perfil")
    @PostMapping(value = "/me/picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> uploadProfilePicture(
            @RequestParam("file") MultipartFile file) {
        // Validar que el archivo no esté vacío
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<String>builder()
                            .success(false)
                            .message("El archivo no puede estar vacío")
                            .build());
        }

        // Validar que sea una imagen
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<String>builder()
                            .success(false)
                            .message("El archivo debe ser una imagen")
                            .build());
        }

        try {
            // Guardar el archivo
            String fileName = fileStorageService.storeFile(file);

            // Construir la URL para acceder al archivo
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/files/download/")
                    .path(fileName)
                    .toUriString();

            // Actualizar el perfil del usuario con la nueva URL de la imagen
            userService.updateProfilePicture(fileDownloadUri);

            return ResponseEntity.ok(ApiResponse.<String>builder()
                    .success(true)
                    .message("Imagen de perfil actualizada correctamente")
                    .data(fileDownloadUri)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.<String>builder()
                            .success(false)
                            .message("Error al subir la imagen: " + e.getMessage())
                            .build());
        }
    }

    @Operation(summary = "Obtiene la imagen de perfil del usuario actual")
    @GetMapping(value = "/me/picture", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public ResponseEntity<?> getProfilePicture() {
        try {
            UserDto user = userService.getCurrentUser();

            if (user.getProfilePictureUrl() == null || user.getProfilePictureUrl().isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Extraer el nombre del archivo de la URL
            String fileName = user.getProfilePictureUrl().substring(
                    user.getProfilePictureUrl().lastIndexOf('/') + 1);

            Resource resource = fileStorageService.loadFileAsResource(fileName);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Ajustar según el tipo de imagen
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.<String>builder()
                            .success(false)
                            .message("Error al cargar la imagen: " + e.getMessage())
                            .build());
        }
    }
}