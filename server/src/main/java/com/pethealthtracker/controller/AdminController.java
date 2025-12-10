package com.pethealthtracker.controller;

import com.pethealthtracker.dto.user.AdminUserRequest;
import com.pethealthtracker.dto.user.UserDto;
import com.pethealthtracker.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Administración de Usuarios", description = "Endpoints para gestionar usuarios del sistema (solo administradores)")
public class AdminController {

    private final AdminService adminService;

    @Operation(
        summary = "Crear un nuevo usuario",
        description = "Crea un nuevo usuario en el sistema con los datos proporcionados. Solo administradores pueden acceder.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "El email ya existe en el sistema"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos de administrador")
        }
    )
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody AdminUserRequest userRequest) {
        UserDto user = adminService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Operation(
        summary = "Obtener todos los usuarios",
        description = "Retorna una lista de todos los usuarios registrados en el sistema. Solo administradores pueden acceder.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos de administrador")
        }
    )
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(
        summary = "Obtener usuario por ID",
        description = "Retorna los detalles de un usuario específico usando su ID. Solo administradores pueden acceder.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Detalles del usuario obtenidos exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos de administrador")
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = adminService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(
        summary = "Actualizar usuario",
        description = "Actualiza los datos de un usuario existente (nombre, apellido, email, contraseña y rol). Solo administradores pueden acceder.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "409", description = "El email ya existe en el sistema"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos de administrador")
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody AdminUserRequest userRequest) {
        UserDto updatedUser = adminService.updateUser(id, userRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(
        summary = "Eliminar usuario",
        description = "Elimina un usuario del sistema. No se puede eliminar a sí mismo. Solo administradores pueden acceder.",
        responses = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "400", description = "No puede eliminarse a sí mismo"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos de administrador")
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
