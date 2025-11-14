package com.pethealthtracker.controller;

import com.pethealthtracker.dto.ApiResponse;
import com.pethealthtracker.dto.auth.JwtAuthResponse;
import com.pethealthtracker.dto.auth.LoginRequest;
import com.pethealthtracker.dto.auth.RegisterRequest;
import com.pethealthtracker.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtAuthResponse>> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest) {
        JwtAuthResponse response = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(ApiResponse.<JwtAuthResponse>builder()
                .success(true)
                .message("User authenticated successfully")
                .data(response)
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<JwtAuthResponse>> registerUser(
            @Valid @RequestBody RegisterRequest registerRequest) {
        JwtAuthResponse response = authService.registerUser(registerRequest);
        return ResponseEntity.ok(ApiResponse.<JwtAuthResponse>builder()
                .success(true)
                .message("User registered successfully")
                .data(response)
                .build());
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestParam String email) {
        authService.requestPasswordReset(email);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Password reset link sent to your email")
                .build());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {
        authService.resetPassword(token, newPassword);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Password reset successfully")
                .build());
    }

    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@RequestParam String token) {
        boolean isVerified = authService.verifyEmail(token);
        if (isVerified) {
            return ResponseEntity.ok(ApiResponse.<Void>builder()
                    .success(true)
                    .message("Email verified successfully")
                    .build());
        } else {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<Void>builder()
                            .success(false)
                            .message("Invalid or expired verification token")
                            .build());
        }
    }
}
