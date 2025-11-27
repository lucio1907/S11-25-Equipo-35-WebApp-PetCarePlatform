package com.pethealthtracker.controller;

import com.pethealthtracker.dto.vaccinations.VaccinationRequestDTO;
import com.pethealthtracker.dto.vaccinations.VaccinationResponseDTO;
import com.pethealthtracker.service.VaccinationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Vacunas", description = "Operaciones CRUD de registro de vacunas por mascota")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/{userId}/pets/{petId}/vaccinations")
public class VaccinationController {
    /*public VaccinationController(){
    }
    @Operation(
            summary = "Obtener vacuna por ID",
            description = "Retorna los detalles de un registro de vacuna específico.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Detalles de la vacuna."),
                    @ApiResponse(responseCode = "404", description = "Vacuna no encontrada o no pertenece a la mascota/usuario.")
            }
    )
    @GetMapping()
    public ResponseEntity<String> getVaccinationById() {
        return ResponseEntity.ok("vaccination");
    }*/
    private final VaccinationService vaccinationService;

    @Operation(
            summary = "Registrar nueva vacuna",
            description = "Añade un nuevo registro de vacuna a la mascota especificada por {petId}.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Vacuna registrada con éxito."),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos."),
                    @ApiResponse(responseCode = "404", description = "Usuario o mascota no encontrado.")
            }
    )
    @PostMapping
    public ResponseEntity<VaccinationResponseDTO> createVaccination(
            @Parameter(description = "ID del usuario propietario", required = true)
            @PathVariable Long userId,
            @Parameter(description = "ID de la mascota", required = true)
            @PathVariable Long petId,
            @Valid @RequestBody VaccinationRequestDTO requestDTO) {
        
        VaccinationResponseDTO createdVaccination = vaccinationService.createVaccination(userId, petId, requestDTO);
        return new ResponseEntity<>(createdVaccination, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Obtener vacuna por ID",
            description = "Retorna los detalles de un registro de vacuna específico.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Detalles de la vacuna."),
                    @ApiResponse(responseCode = "404", description = "Vacuna no encontrada o no pertenece a la mascota/usuario.")
            }
    )
    @GetMapping("/{vaccinationId}")
    public ResponseEntity<VaccinationResponseDTO> getVaccinationById(
            @Parameter(description = "ID del usuario propietario", required = true)
            @PathVariable Long userId,
            @Parameter(description = "ID de la mascota", required = true)
            @PathVariable Long petId,
            @Parameter(description = "ID del registro de vacuna", required = true)
            @PathVariable Long vaccinationId) {
        
        VaccinationResponseDTO vaccination = vaccinationService.getVaccinationById(userId, petId, vaccinationId);
        return ResponseEntity.ok(vaccination);
    }

    @Operation(
            summary = "Listar todas las vacunas de la mascota",
            description = "Retorna una lista de todos los registros de vacunas de la mascota especificada.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de vacunas (puede estar vacía)."),
                    @ApiResponse(responseCode = "404", description = "Usuario o mascota no encontrado.")
            }
    )
    @GetMapping
    public ResponseEntity<List<VaccinationResponseDTO>> getVaccinationsByPetId(
            @Parameter(description = "ID del usuario propietario", required = true)
            @PathVariable Long userId,
            @Parameter(description = "ID de la mascota", required = true)
            @PathVariable Long petId) {
        
        List<VaccinationResponseDTO> vaccinations = vaccinationService.getVaccinationsByPetId(userId, petId);
        return ResponseEntity.ok(vaccinations);
    }

    @Operation(
            summary = "Actualizar registro de vacuna",
            description = "Actualiza el registro de vacuna específico.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Registro de vacuna actualizado con éxito."),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos."),
                    @ApiResponse(responseCode = "404", description = "Vacuna no encontrada o no pertenece a la mascota/usuario.")
            }
    )
    @PutMapping("/{vaccinationId}")
    public ResponseEntity<VaccinationResponseDTO> updateVaccination(
            @Parameter(description = "ID del usuario propietario", required = true)
            @PathVariable Long userId,
            @Parameter(description = "ID de la mascota", required = true)
            @PathVariable Long petId,
            @Parameter(description = "ID del registro de vacuna a actualizar", required = true)
            @PathVariable Long vaccinationId,
            @Valid @RequestBody VaccinationRequestDTO requestDTO) {
        
        VaccinationResponseDTO updatedVaccination = vaccinationService.updateVaccination(userId, petId, vaccinationId, requestDTO);
        return ResponseEntity.ok(updatedVaccination);
    }

    @Operation(
            summary = "Eliminar registro de vacuna",
            description = "Elimina lógicamente el registro de vacuna especificado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Registro de vacuna eliminado exitosamente."),
                    @ApiResponse(responseCode = "404", description = "Vacuna no encontrada o no pertenece a la mascota/usuario.")
            }
    )
    @DeleteMapping("/{vaccinationId}")
    public ResponseEntity<Map<String, Object>> deleteVaccination(
            @Parameter(description = "ID del usuario propietario", required = true)
            @PathVariable Long userId,
            @Parameter(description = "ID de la mascota", required = true)
            @PathVariable Long petId,
            @Parameter(description = "ID del registro de vacuna a eliminar", required = true)
            @PathVariable Long vaccinationId) {
        
        vaccinationService.deleteVaccination(userId, petId, vaccinationId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Registro de vacuna eliminado exitosamente");
        return ResponseEntity.ok(response);
    }
}