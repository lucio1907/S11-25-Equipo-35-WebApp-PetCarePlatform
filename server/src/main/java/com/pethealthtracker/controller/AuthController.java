package com.pethealthtracker.controller;

import com.pethealthtracker.dto.ApiResponse;
import com.pethealthtracker.dto.auth.JwtAuthResponse;
import com.pethealthtracker.dto.auth.LoginRequest;
import com.pethealthtracker.dto.auth.RegisterRequest;
import com.pethealthtracker.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import jakarta.validation.Valid;
/**
 * Controlador para gestionar autenticación y registro de usuarios.
 * Proporciona endpoints para inicio de sesión, registro, recuperación de contraseña y verificación de correo electrónico.
 * 
 * @apiNote Tabla de base de datos: users
 */
@Tag(name = "Autenticación (users)", description = "API para autenticación y gestión de cuentas")
@Validated
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Autentica un usuario y devuelve un token JWT")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtAuthResponse>> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest) {
        JwtAuthResponse response = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(ApiResponse.<JwtAuthResponse>builder()
                .success(true)
                .message("Inicio de sesión exitoso")
                .data(response)
                .build());
    }

    @Operation(summary = "Registra un nuevo usuario en el sistema")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<JwtAuthResponse>> registerUser(
            @Valid @RequestBody RegisterRequest registerRequest) {
        JwtAuthResponse response = authService.registerUser(registerRequest);
        return ResponseEntity.ok(ApiResponse.<JwtAuthResponse>builder()
                .success(true)
                .message("Usuario registrado exitosamente")
                .data(response)
                .build());
    }

    @Operation(summary = "Solicita el restablecimiento de contraseña")
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @RequestParam @NotBlank @Email String email) {
        authService.requestPasswordReset(email);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Se ha enviado un enlace de restablecimiento de contraseña al correo electrónico proporcionado")
                .build());
    }

    @Operation(summary = "Restablece la contraseña usando el token de restablecimiento")
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @RequestParam @NotBlank String token,
            @RequestParam @NotBlank String newPassword) {
        authService.resetPassword(token, newPassword);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Contraseña restablecida exitosamente")
                .build());
    }

    @Operation(summary = "Verifica el correo electrónico del usuario usando el token de verificación")
    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(
            @RequestParam @NotBlank String token) {
        boolean isVerified = authService.verifyEmail(token);
        if (isVerified) {
            return ResponseEntity.ok(ApiResponse.<Void>builder()
                    .success(true)
                    .message("Correo electrónico verificado exitosamente")
                    .build());
        } else {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<Void>builder()
                            .success(false)
                            .message("Token de verificación inválido o expirado")
                            .build());
        }
    }
}
