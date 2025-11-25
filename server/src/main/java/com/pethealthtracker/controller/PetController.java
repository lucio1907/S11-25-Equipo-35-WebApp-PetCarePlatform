package com.pethealthtracker.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pethealthtracker.dto.pets.PetRequestDTO;
import com.pethealthtracker.dto.pets.PetResponseDTO;
import com.pethealthtracker.service.PetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Mascotas", description = "Operaciones CRUD de mascotas")
@RestController
@RequestMapping("/user/{userId}/pets") // RUTA BASE ANIDADA
public class PetController {
    private final PetService petService;

    // Inyección por constructor
    public PetController(PetService petService) {
        this.petService = petService;
    }

    // -----------------------------------------------------
    // 1. CREAR MASCOTA (POST) - URL: /api/user/{userId}/pets
    // -----------------------------------------------------
    @Operation(
        summary="Crear nueva mascota",
        description="Registra una nueva mascota y la asocia al usuario proporcionado en la URL.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Mascota creada con éxito."),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos."),
            @ApiResponse(responseCode = "404", description = "Usuario propietario no encontrado.")
        }
    )
    @PostMapping
    public ResponseEntity<PetResponseDTO> createNewPet(
        @Parameter(description = "ID del usuario propietario", required = true)
        @PathVariable Long userId, 
        @Valid @RequestBody PetRequestDTO petRequestDTO) {
        
        PetResponseDTO createdPet = petService.createPet(userId, petRequestDTO);
        return new ResponseEntity<>(createdPet, HttpStatus.CREATED);
    }

    // -----------------------------------------------------
    // 2. OBTENER MASCOTA POR NOMBRE (GET) - URL: /api/user/{userId}/pets/{name}
    // -----------------------------------------------------
    @Operation(
        summary = "Obtener mascota por ID de usuario y nombre de mascota",
        description = "Retorna los detalles de una mascota específica asociada al usuario.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Detalles de la mascota."),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada o no pertenece al usuario.")
        }
    )
    @GetMapping("/{name}")
    public ResponseEntity<PetResponseDTO> getPetByName(
        @PathVariable Long userId, // De la ruta base
        @Parameter(description = "Nombre de la mascota a buscar", required = true)
        @PathVariable String name) {
        
        PetResponseDTO pet = petService.getPetByNameAndUserId(name, userId); 
        return ResponseEntity.ok(pet);
    }
    
    // -----------------------------------------------------
    // 3. LISTAR TODAS LAS MASCOTAS DEL USUARIO (GET) - URL: /api/user/{userId}/pets
    // -----------------------------------------------------
    @Operation(
        summary = "Listar todas las mascotas del usuario",
        description = "Retorna una lista de todas las mascotas asociadas al usuario en la URL.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de mascotas (puede estar vacía)."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
        }
    )
    @GetMapping
    public ResponseEntity<List<PetResponseDTO>> getPetsByUserId(
        @Parameter(description = "ID del usuario propietario", required = true)
        @PathVariable Long userId) {
        
        List<PetResponseDTO> pets = petService.getPetsByUserId(userId);            
        return ResponseEntity.ok(pets);
    }

    // -----------------------------------------------------
    // 4. ACTUALIZAR MASCOTA (PUT) - URL: /api/user/{userId}/pets/{id}
    // -----------------------------------------------------
    @Operation(
        summary = "Actualizar mascota",
        description = "Actualiza todos los campos de una mascota existente (el ID del propietario no se modifica).",
        responses = {
            @ApiResponse(responseCode = "200", description = "Mascota actualizada con éxito."),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos."),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada o no pertenece al usuario.")
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<PetResponseDTO> updatePet(
            @PathVariable Long userId, // De la ruta base
            @Parameter(description = "ID de la mascota a actualizar", required = true)
            @PathVariable Long id,
            @Valid @RequestBody PetRequestDTO requestDTO) {
        
        PetResponseDTO updatedPet = petService.updatePet(id, userId, requestDTO);
        return ResponseEntity.ok(updatedPet);
    }

    // -----------------------------------------------------
    // 5. ELIMINAR/DESACTIVAR MASCOTA (DELETE) - URL: /api/user/{userId}/pets/{id}
    // -----------------------------------------------------
    @Operation(
        summary = "Desactivar mascota (Eliminación Lógica)",
        description = "Marca la mascota como inactiva (is_active = false) en la base de datos.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Mascota desactivada exitosamente."),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada o no pertenece al usuario.")
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePet(
        @PathVariable Long userId, // De la ruta base
        @Parameter(description = "ID de la mascota a desactivar", required = true)
        @PathVariable Long id) {
        
        petService.softDeletePet(id, userId);
        Map<String, Object> response = new HashMap<>();
        response.put("Success", true);
        response.put("message", "Mascota desactivada exitosamente"); 
        return ResponseEntity.ok(response);
    }
}