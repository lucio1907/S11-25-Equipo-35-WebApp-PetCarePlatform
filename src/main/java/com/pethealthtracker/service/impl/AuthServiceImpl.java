package com.pethealthtracker.service.impl;

import com.pethealthtracker.dto.auth.JwtAuthResponse;
import com.pethealthtracker.dto.auth.LoginRequest;
import com.pethealthtracker.dto.auth.RegisterRequest;
import com.pethealthtracker.dto.user.UserDto;
import com.pethealthtracker.exception.BadRequestException;
import com.pethealthtracker.exception.ResourceNotFoundException;
import com.pethealthtracker.mapper.UserMapper;
import com.pethealthtracker.model.User;
import com.pethealthtracker.repository.UserRepository;
import com.pethealthtracker.security.JwtTokenProvider;
import com.pethealthtracker.security.UserPrincipal;
import com.pethealthtracker.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public JwtAuthResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
        
        UserDto userDto = userMapper.toDto(user);
        
        return new JwtAuthResponse(jwt, userDto);
    }

    @Override
    @Transactional
    public JwtAuthResponse registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("Email address already in use!");
        }

        // Create new user's account
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setPhone(registerRequest.getPhone());
        user.setEmailVerified(false);
        
        User result = userRepository.save(user);
        
        // Generate JWT token
        UserPrincipal userPrincipal = UserPrincipal.create(result);
        String jwt = tokenProvider.generateToken(
                new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities())
        );
        
        UserDto userDto = userMapper.toDto(result);
        
        return new JwtAuthResponse(jwt, userDto);
    }

    @Override
    @Transactional
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
                
        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setResetTokenExpires(java.time.LocalDateTime.now().plusHours(1));
        userRepository.save(user);
        
        // TODO: Send email with password reset link
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid or expired password reset token"));
                
        if (user.getResetTokenExpires().isBefore(java.time.LocalDateTime.now())) {
            throw new BadRequestException("Password reset token has expired");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setResetTokenExpires(null);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public boolean verifyEmail(String verificationToken) {
        // TODO: Implement email verification logic
        // This would typically involve finding a user by verification token
        // and updating their email verification status
        return false;
    }
}
