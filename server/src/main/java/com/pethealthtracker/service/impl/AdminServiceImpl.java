package com.pethealthtracker.service.impl;

import com.pethealthtracker.dto.user.AdminUserRequest;
import com.pethealthtracker.dto.user.UserDto;
import com.pethealthtracker.exception.ResourceAlreadyExistsException;
import com.pethealthtracker.exception.ResourceNotFoundException;
import com.pethealthtracker.model.User;
import com.pethealthtracker.model.enums.Role;
import com.pethealthtracker.repository.UserRepository;
import com.pethealthtracker.security.UserPrincipal;
import com.pethealthtracker.service.AdminService;
import com.pethealthtracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto createUser(AdminUserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new ResourceAlreadyExistsException("Email", "email", userRequest.getEmail());
        }

        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setEmailVerified(true);

        if (userRequest.isAdmin()) {
            user.addRole(Role.ROLE_ADMIN);
        }

        User savedUser = userRepository.save(user);
        return userService.convertToDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userService::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return userService.convertToDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, AdminUserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());

        if (!user.getEmail().equals(userRequest.getEmail()) &&
                userRepository.existsByEmail(userRequest.getEmail())) {
            throw new ResourceAlreadyExistsException("Email", "email", userRequest.getEmail());
        }
        user.setEmail(userRequest.getEmail());

        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        if (userRequest.isAdmin()) {
            user.addRole(Role.ROLE_ADMIN);
        } else {
            user.getRoles().remove(Role.ROLE_ADMIN);
        }

        User updatedUser = userRepository.save(user);
        return userService.convertToDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User currentUser = getCurrentUser();
        if (currentUser.getId().equals(id)) {
            throw new IllegalArgumentException("No puedes eliminarte a ti mismo");
        }
        userRepository.deleteById(id);
    }

    private User getCurrentUser() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
}
