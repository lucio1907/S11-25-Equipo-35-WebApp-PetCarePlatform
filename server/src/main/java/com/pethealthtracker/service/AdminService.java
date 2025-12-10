package com.pethealthtracker.service;

import com.pethealthtracker.dto.user.AdminUserRequest;
import com.pethealthtracker.dto.user.UserDto;

import java.util.List;

public interface AdminService {
    UserDto createUser(AdminUserRequest userRequest);
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto updateUser(Long id, AdminUserRequest userRequest);
    void deleteUser(Long id);
}
