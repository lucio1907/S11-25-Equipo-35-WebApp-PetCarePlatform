package com.pethealthtracker.mapper;

import com.pethealthtracker.dto.user.UserDto;
import com.pethealthtracker.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mapper para convertir entre User y UserDto.
 */
@Component
public class UserMapper {

    /**
     * Convierte un User a un UserDto.
     *
     * @param user el usuario a convertir
     * @return el DTO del usuario
     */
    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .profilePictureUrl(user.getProfilePictureUrl())
                .emailVerified(user.getEmailVerified())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    /**
     * Convierte un UserDto a un User.
     *
     * @param userDto el DTO del usuario a convertir
     * @return el usuario
     */
    public User toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        LocalDateTime now = LocalDateTime.now();
        
        // Create user using the builder
        User user = User.builder()
                .email(userDto.getEmail())
                .password("defaultPassword") // Make sure to set a secure password or handle this differently
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .phone(userDto.getPhone())
                .profilePictureUrl(userDto.getProfilePictureUrl())
                .emailVerified(userDto.getEmailVerified() != null ? userDto.getEmailVerified() : false)
                .build();
                
        // Set the ID from the DTO if it exists
        if (userDto.getId() != null) {
            user.setId(userDto.getId());
        }
        
        // Set the timestamps
        user.setCreatedAt(userDto.getCreatedAt() != null ? userDto.getCreatedAt() : now);
        user.setUpdatedAt(now);
        
        return user;
    }
}