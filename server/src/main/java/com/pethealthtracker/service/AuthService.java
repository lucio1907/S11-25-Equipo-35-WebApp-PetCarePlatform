package com.pethealthtracker.service;

import com.pethealthtracker.dto.auth.JwtAuthResponse;
import com.pethealthtracker.dto.auth.LoginRequest;
import com.pethealthtracker.dto.auth.RegisterRequest;

public interface AuthService {
    JwtAuthResponse authenticateUser(LoginRequest loginRequest);
    JwtAuthResponse registerUser(RegisterRequest registerRequest);
    void requestPasswordReset(String email);
    void resetPassword(String token, String newPassword);
    boolean verifyEmail(String verificationToken);
}
