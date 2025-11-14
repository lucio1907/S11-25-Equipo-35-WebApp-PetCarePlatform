package com.pethealthtracker.controller;

import com.pethealthtracker.dto.ApiResponse;
import com.pethealthtracker.dto.user.UserDto;
import com.pethealthtracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Controlador para gestionar las operaciones de usuario.
 */

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    
    /**
     * Obtiene el usuario actualmente autenticado.
     *
     * @return ResponseEntity con los datos del usuario actual
     */

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getCurrentUser() {
        UserDto user = userService.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.<UserDto>builder()
                .success(true)
                .data(user)
                .build());
    }
    
    /**
     * Obtiene un usuario por su ID (solo para administradores).
     *
     * @param id ID del usuario a buscar
     * @return ResponseEntity con los datos del usuario encontrado
     */

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.<UserDto>builder()
                .success(true)
                .data(user)
                .build());
    }
    
    /**
     * Actualiza los datos del usuario actual.
     *
     * @param userDto DTO con los datos actualizados del usuario
     * @return ResponseEntity con los datos actualizados del usuario
     */

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(
            @Valid @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(userDto);
        return ResponseEntity.ok(ApiResponse.<UserDto>builder()
                .success(true)
                .message("User updated successfully")
                .data(updatedUser)
                .build());
    }
    
    /**
     * Elimina el usuario actual.
     *
     * @return ResponseEntity indicando el éxito de la operación
     */

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteUser() {
        userService.deleteUser();
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("User deleted successfully")
                .build());
    }
    
    /**
     * Obtiene todos los usuarios (solo para administradores).
     *
     * @return ResponseEntity con la lista de todos los usuarios
     */

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.<List<UserDto>>builder()
                .success(true)
                .data(users)
                .build());
    }
    
    /**
     * Obtiene el perfil del usuario actual.
     *
     * @return ResponseEntity con los datos del perfil del usuario
     */

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserDto>> getUserProfile() {
        UserDto user = userService.getUserProfile();
        return ResponseEntity.ok(ApiResponse.<UserDto>builder()
                .success(true)
                .data(user)
                .build());
    }
    
    /**
     * Actualiza el perfil del usuario actual.
     *
     * @param userDto DTO con los datos actualizados del perfil
     * @return ResponseEntity con los datos actualizados del perfil
     */

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserDto>> updateUserProfile(
            @Valid @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUserProfile(userDto);
        return ResponseEntity.ok(ApiResponse.<UserDto>builder()
                .success(true)
                .message("Profile updated successfully")
                .data(updatedUser)
                .build());
    }
    
    /**
     * Cambia la contraseña del usuario actual.
     *
     * @param currentPassword Contraseña actual
     * @param newPassword Nueva contraseña
     * @return ResponseEntity indicando el éxito de la operación
     */

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword) {
        userService.changePassword(currentPassword, newPassword);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Password changed successfully")
                .build());
    }
    
    /**
     * Sube una nueva imagen de perfil para el usuario actual.
     *
     * @param file Archivo de imagen a subir
     * @return ResponseEntity indicando el éxito de la operación
     * @throws IOException Si ocurre un error al procesar el archivo
     */

    @PostMapping("/profile/picture")
    public ResponseEntity<ApiResponse<Void>> uploadProfilePicture(
            @RequestParam("file") MultipartFile file) throws IOException {
        // En una aplicación real, aquí se subiría el archivo a un servicio de almacenamiento
        // (como AWS S3) y se obtendría la URL, luego se llamaría a userService.updateProfilePicture(imageUrl)
        // Por ahora, simularemos una carga exitosa
        String imageUrl = "https://example.com/profile-pictures/" + file.getOriginalFilename();
        userService.updateProfilePicture(imageUrl);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Profile picture updated successfully")
                .build());
    }
}
