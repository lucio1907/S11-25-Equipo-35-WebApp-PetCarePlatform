package com.pethealthtracker.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private UserDto user;
    
    public JwtAuthResponse(String accessToken, UserDto user) {
        this.accessToken = accessToken;
        this.user = user;
    }
}
