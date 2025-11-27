package com.pethealthtracker.service;

import com.pethealthtracker.dto.user.UserDto;

import java.util.List;

public interface UserService {
    UserDto getCurrentUser();
    UserDto getUserById(Long id);
    UserDto updateUser(UserDto userDto);
    void deleteUser();
    List<UserDto> getAllUsers();
    UserDto getUserProfile();
    UserDto updateUserProfile(UserDto userDto);
    void changePassword(String currentPassword, String newPassword);
    void updateProfilePicture(String imageUrl);
}
