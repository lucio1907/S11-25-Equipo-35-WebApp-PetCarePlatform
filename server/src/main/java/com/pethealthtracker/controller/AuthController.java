package com.pethealthtracker.controller;

import com.pethealthtracker.dto.ApiResponse;
import com.pethealthtracker.dto.auth.JwtAuthResponse;
import com.pethealthtracker.dto.auth.LoginRequest;
import com.pethealthtracker.dto.auth.RegisterRequest;
import com.pethealthtracker.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para gerenciar autenticação e registro de usuários.
 * Fornece endpoints para login, registro, recuperação de senha e verificação de e-mail.
 */
@Tag(name = "Autenticação", description = "API para autenticação e gerenciamento de contas")
@Validated
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Autentica um usuário e retorna um token JWT")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtAuthResponse>> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest) {
        JwtAuthResponse response = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(ApiResponse.<JwtAuthResponse>builder()
                .success(true)
                .message("Autenticação realizada com sucesso")
                .data(response)
                .build());
    }

    @Operation(summary = "Registra um novo usuário")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<JwtAuthResponse>> registerUser(
            @Valid @RequestBody RegisterRequest registerRequest) {
        JwtAuthResponse response = authService.registerUser(registerRequest);
        return ResponseEntity.ok(ApiResponse.<JwtAuthResponse>builder()
                .success(true)
                .message("Usuário registrado com sucesso")
                .data(response)
                .build());
    }

    @Operation(summary = "Solicita a redefinição de senha")
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @RequestParam @NotBlank @Email String email) {
        authService.requestPasswordReset(email);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Link de redefinição de senha enviado para o e-mail")
                .build());
    }

    @Operation(summary = "Redefine a senha usando o token de redefinição")
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @RequestParam @NotBlank String token,
            @RequestParam @NotBlank String newPassword) {
        authService.resetPassword(token, newPassword);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Senha redefinida com sucesso")
                .build());
    }

    @Operation(summary = "Verifica o e-mail do usuário usando o token de verificação")
    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(
            @RequestParam @NotBlank String token) {
        boolean isVerified = authService.verifyEmail(token);
        if (isVerified) {
            return ResponseEntity.ok(ApiResponse.<Void>builder()
                    .success(true)
                    .message("E-mail verificado com sucesso")
                    .build());
        } else {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<Void>builder()
                            .success(false)
                            .message("Token de verificação inválido ou expirado")
                            .build());
        }
    }
}
