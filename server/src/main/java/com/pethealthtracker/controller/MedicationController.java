package com.pethealthtracker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pethealthtracker.dto.medications.MedicationRequestDTO;
import com.pethealthtracker.dto.medications.MedicationResponseDTO;
import com.pethealthtracker.exception.ResourceNotFoundException;
import com.pethealthtracker.service.MedicationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "Medicamentos", description = "Gestión de medicamentos y tratamientos para las mascotas.")
@RestController
@RequestMapping("/medications")
@RequiredArgsConstructor
public class MedicationController {
    private final MedicationService medicationService;

    // -----------------------------------------------------
    // 1. CREAR MEDICAMENTO
    // -----------------------------------------------------
    @Operation(
        summary = "Registrar un nuevo medicamento",
        description = "Crea un registro de medicamento asociado a una mascota (el ID de la mascota viene en el cuerpo).",
        responses = {
            @ApiResponse(responseCode = "201", description = "Medicamento creado exitosamente."),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos."),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada.")
        })

        @PostMapping
        public ResponseEntity<MedicationResponseDTO> createMedication(@Valid @RequestBody MedicationRequestDTO requestDTO) {
            MedicationResponseDTO createdMedication = medicationService.createMedication(requestDTO);

            return new ResponseEntity<>(createdMedication, HttpStatus.CREATED);
        }
    // -----------------------------------------------------
    // 2. OBTENER MEDICAMENTO POR ID (GET)
    // -----------------------------------------------------
    @Operation(summary = "Obtener medicamento por ID", description = "Retorna los detalles de un medicamento específico.", responses = {
            @ApiResponse(responseCode = "200", description = "Detalles del medicamento"),
            @ApiResponse(responseCode = "404", description = "Medicamento no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MedicationResponseDTO> getMedicationById(
            @Parameter(description = "ID del medicamento", required = true) @PathVariable Long id) {
        MedicationResponseDTO medication = medicationService.getMedicationsById(id);
        return ResponseEntity.ok(medication);
    }

    // -----------------------------------------------------
    // 3. LISTAR MEDICAMENTOS POR MASCOTA (GET)
    // -----------------------------------------------------
    @Operation(summary = "Listar medicamentos de una mascota", description = "Obtiene todos los medicamentos asociados a una mascota específica.", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de medicamentos."),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada."),
            @ApiResponse(responseCode = "404", description = "Mascota no tiene medicamentos registrados.")
    })
    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<MedicationResponseDTO>> getMedicationsByPetId(
            @Parameter(description = "ID de la mascota", required = true) @PathVariable Long petId) {
        List<MedicationResponseDTO> medications = medicationService.getMedicationsByPetId(petId);
        
        if (medications.isEmpty()) {
            throw new ResourceNotFoundException("Esta mascota no tiene medicamentos registrados.");
        }

        return ResponseEntity.ok(medications);
    }

    // -----------------------------------------------------
    // 4. ACTUALIZAR MEDICAMENTO (PUT)
    // -----------------------------------------------------
    @Operation(summary = "Actualizar medicamento", description = "Actualiza los datos de un medicamento existente.", responses = {
            @ApiResponse(responseCode = "200", description = "Medicamento actualizado"),
            @ApiResponse(responseCode = "404", description = "Medicamento no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MedicationResponseDTO> updateMedication(
            @Parameter(description = "ID del medicamento a actualizar", required = true) @PathVariable Long id,
            @Valid @RequestBody MedicationRequestDTO requestDTO) {
        MedicationResponseDTO updatedMedication = medicationService.updateMedication(id, requestDTO);
        return ResponseEntity.ok(updatedMedication);
    }

    // -----------------------------------------------------
    // 5. ELIMINAR/DESACTIVAR MEDICAMENTO (DELETE)
    // -----------------------------------------------------
    @Operation(
        summary = "Eliminar medicamento (Lógico)",
        description = "Marca el medicamento como inactivo",
        responses = {
            @ApiResponse(responseCode = "204", description = "Medicamento eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Medicamento no encontrado")
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(@Parameter(description = "ID del medicamento a eliminar", required = true) @PathVariable Long id){
        medicationService.deleteMedication(id);
        return ResponseEntity.noContent().build();
    }

}
