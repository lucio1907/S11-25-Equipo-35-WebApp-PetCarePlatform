package com.pethealthtracker.controller;

import com.pethealthtracker.dto.ApiResponse;
import com.pethealthtracker.dto.feeding.FeedingScheduleDto;
import com.pethealthtracker.service.FeedingScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador para gestionar los horarios de alimentación de las mascotas.
 * Permite a los usuarios autenticados gestionar los horarios de alimentación de sus mascotas.
 * 
 * @apiNote Tabla de base de datos: feeding_schedules
 */
@Tag(name = "Horarios de Alimentación (feeding_schedules)", 
     description = "API para la gestión de horarios de alimentación de mascotas")
@SecurityRequirement(name = "bearerAuth")

@RestController
@RequestMapping("/pets/{petId}/feeding-schedules")
public class FeedingScheduleController {

    private final FeedingScheduleService feedingScheduleService;

    public FeedingScheduleController(FeedingScheduleService feedingScheduleService) {
        this.feedingScheduleService = feedingScheduleService;
    }

    @Operation(summary = "Obtiene todos los horarios de alimentación de una mascota")
    @GetMapping
    public ResponseEntity<ApiResponse<List<FeedingScheduleDto>>> getAllFeedingSchedules(
            @PathVariable Long petId) {
        return ResponseEntity.ok(ApiResponse.<List<FeedingScheduleDto>>builder()
                .success(true)
                .data(feedingScheduleService.getFeedingSchedulesByPetId(petId))
                .build());
    }

    @Operation(summary = "Obtiene los horarios de alimentación activos de una mascota")
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<FeedingScheduleDto>>> getActiveFeedingSchedules(
            @PathVariable Long petId) {
        return ResponseEntity.ok(ApiResponse.<List<FeedingScheduleDto>>builder()
                .success(true)
                .data(feedingScheduleService.getActiveFeedingSchedulesByPetId(petId))
                .build());
    }

    @Operation(summary = "Obtiene un horario de alimentación por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FeedingScheduleDto>> getFeedingScheduleById(
            @PathVariable Long petId,
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<FeedingScheduleDto>builder()
                .success(true)
                .data(feedingScheduleService.getFeedingScheduleById(id))
                .build());
    }

    @Operation(summary = "Crea un nuevo horario de alimentación para una mascota")
    @PostMapping
    public ResponseEntity<ApiResponse<FeedingScheduleDto>> createFeedingSchedule(
            @PathVariable Long petId,
            @Valid @RequestBody FeedingScheduleDto feedingScheduleDto) {
        feedingScheduleDto.setPetId(petId);
        FeedingScheduleDto createdSchedule = feedingScheduleService.createFeedingSchedule(feedingScheduleDto);
        return new ResponseEntity<>(ApiResponse.<FeedingScheduleDto>builder()
                .success(true)
                .message("Horario de alimentación creado correctamente")
                .data(createdSchedule)
                .build(), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualiza un horario de alimentación existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FeedingScheduleDto>> updateFeedingSchedule(
            @PathVariable Long petId,
            @PathVariable Long id,
            @Valid @RequestBody FeedingScheduleDto feedingScheduleDto) {
        feedingScheduleDto.setPetId(petId);
        feedingScheduleDto.setId(id);
        return ResponseEntity.ok(ApiResponse.<FeedingScheduleDto>builder()
                .message("Horario de alimentación actualizado correctamente")
                .data(feedingScheduleService.updateFeedingSchedule(id, feedingScheduleDto))
                .build());
    }

    @Operation(summary = "Elimina un horario de alimentación")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFeedingSchedule(@PathVariable Long id) {
        feedingScheduleService.deleteFeedingSchedule(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Horario de alimentación eliminado correctamente")
                .build());
    }
}