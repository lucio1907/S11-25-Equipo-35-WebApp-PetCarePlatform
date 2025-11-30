package com.pethealthtracker.controller;

import com.pethealthtracker.dto.veterinary_visits.VeterinaryVisitRequestDTO;
import com.pethealthtracker.dto.veterinary_visits.VeterinaryVisitResponseDTO;
import com.pethealthtracker.service.VeterinaryVisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Visitas Veterinarias", description = "Operaciones CRUD de registros de visitas veterinarias")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/pets/{petId}/veterinary-visits")
public class VeterinaryVisitController {

    private final VeterinaryVisitService veterinaryVisitService;


    @Operation(
        summary = "Registrar nueva visita veterinaria",
        description = "Añade un nuevo registro de visita veterinaria a la mascota especificada.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Visita veterinaria registrada con éxito."),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos."),
            @ApiResponse(responseCode = "404", description = "Usuario o mascota no encontrado.")
        }
    )
    @PostMapping
    public ResponseEntity<VeterinaryVisitResponseDTO> createVeterinaryVisit(
            @Parameter(description = "ID del usuario propietario", required = true)
            @PathVariable Long userId,
            @Parameter(description = "ID de la mascota", required = true)
            @PathVariable Long petId,
            @Valid @RequestBody VeterinaryVisitRequestDTO requestDTO) {
        
        VeterinaryVisitResponseDTO createdVisit = veterinaryVisitService.createVeterinaryVisit(userId, petId, requestDTO);
        return new ResponseEntity<>(createdVisit, HttpStatus.CREATED);
    }

    @Operation(
        summary = "Obtener visita veterinaria por ID",
        description = "Retorna los detalles de un registro de visita veterinaria específico.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Detalles de la visita veterinaria."),
            @ApiResponse(responseCode = "404", description = "Visita no encontrada o no pertenece a la mascota/usuario.")
        }
    )
    @GetMapping("/{visitId}")
    public ResponseEntity<VeterinaryVisitResponseDTO> getVeterinaryVisitById(
            @Parameter(description = "ID del usuario propietario", required = true)
            @PathVariable Long userId,
            @Parameter(description = "ID de la mascota", required = true)
            @PathVariable Long petId,
            @Parameter(description = "ID del registro de visita veterinaria", required = true)
            @PathVariable Long visitId) {
        
        VeterinaryVisitResponseDTO visit = veterinaryVisitService.getVeterinaryVisitById(userId, petId, visitId);
        return ResponseEntity.ok(visit);
    }

    @Operation(
        summary = "Listar todas las visitas veterinarias de la mascota",
        description = "Retorna una lista de todos los registros de visitas veterinarias de la mascota especificada.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de visitas veterinarias (puede estar vacía)."),
            @ApiResponse(responseCode = "404", description = "Usuario o mascota no encontrado.")
        }
    )
    @GetMapping
    public ResponseEntity<List<VeterinaryVisitResponseDTO>> getVeterinaryVisitsByPetId(
            @Parameter(description = "ID del usuario propietario", required = true)
            @PathVariable Long userId,
            @Parameter(description = "ID de la mascota", required = true)
            @PathVariable Long petId) {
        
        List<VeterinaryVisitResponseDTO> visits = veterinaryVisitService.getVeterinaryVisitsByPetId(userId, petId);
        return ResponseEntity.ok(visits);
    }

    @Operation(
        summary = "Actualizar registro de visita veterinaria",
        description = "Actualiza el registro de visita veterinaria específico.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Registro de visita actualizado con éxito."),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos."),
            @ApiResponse(responseCode = "404", description = "Visita no encontrada o no pertenece a la mascota/usuario.")
        }
    )
    @PutMapping("/{visitId}")
    public ResponseEntity<VeterinaryVisitResponseDTO> updateVeterinaryVisit(
            @Parameter(description = "ID del usuario propietario", required = true)
            @PathVariable Long userId,
            @Parameter(description = "ID de la mascota", required = true)
            @PathVariable Long petId,
            @Parameter(description = "ID del registro de visita a actualizar", required = true)
            @PathVariable Long visitId,
            @Valid @RequestBody VeterinaryVisitRequestDTO requestDTO) {
        
        VeterinaryVisitResponseDTO updatedVisit = veterinaryVisitService.updateVeterinaryVisit(
            userId, petId, visitId, requestDTO);
        return ResponseEntity.ok(updatedVisit);
    }

    @Operation(
        summary = "Eliminar registro de visita veterinaria",
        description = "Elimina lógicamente el registro de visita veterinaria especificado.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Registro de visita eliminado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Visita no encontrada o no pertenece a la mascota/usuario.")
        }
    )
    @DeleteMapping("/{visitId}")
    public ResponseEntity<Map<String, Object>> deleteVeterinaryVisit(
            @Parameter(description = "ID del usuario propietario", required = true)
            @PathVariable Long userId,
            @Parameter(description = "ID de la mascota", required = true)
            @PathVariable Long petId,
            @Parameter(description = "ID del registro de visita a eliminar", required = true)
            @PathVariable Long visitId) {
        
        veterinaryVisitService.deleteVeterinaryVisit(userId, petId, visitId);
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Registro de visita veterinaria eliminado exitosamente"
        ));
    }
}
