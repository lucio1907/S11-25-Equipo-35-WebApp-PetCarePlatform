package com.pethealthtracker.controller;

import com.pethealthtracker.dto.ApiResponse;
import com.pethealthtracker.dto.feeding.FeedingRecordDto;
import com.pethealthtracker.mapper.FeedingRecordMapper;
import com.pethealthtracker.model.FeedingRecord;
import com.pethealthtracker.service.FeedingRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Registros de Alimentación (Feeding Records)", 
     description = "API para gestionar los registros de alimentación de las mascotas")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/pets/{petId}/feeding-records")
@RequiredArgsConstructor
public class FeedingRecordController {

    private final FeedingRecordService feedingRecordService;
    private final FeedingRecordMapper feedingRecordMapper;

    @Operation(summary = "Obtener todos los registros de alimentación de una mascota")
    @GetMapping
    public ResponseEntity<ApiResponse<List<FeedingRecordDto>>> getFeedingRecords(
            @PathVariable Long petId) {
        List<FeedingRecord> records = feedingRecordService.getFeedingRecordsByPetId(petId);
        return ResponseEntity.ok(ApiResponse.<List<FeedingRecordDto>>builder()
                .success(true)
                .data(records.stream().map(feedingRecordMapper::toDto).toList())
                .build());
    }

    @Operation(summary = "Obtener registros de alimentación por rango de fechas")
    @GetMapping("/date-range")
    public ResponseEntity<ApiResponse<List<FeedingRecordDto>>> getFeedingRecordsByDateRange(
            @PathVariable Long petId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        List<FeedingRecord> records = feedingRecordService.getFeedingRecordsByPetIdAndDateRange(petId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.<List<FeedingRecordDto>>builder()
                .success(true)
                .data(records.stream().map(feedingRecordMapper::toDto).toList())
                .build());
    }

    @Operation(summary = "Obtener un registro de alimentación por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FeedingRecordDto>> getFeedingRecord(
            @PathVariable Long petId,
            @PathVariable Long id) {
        FeedingRecord record = feedingRecordService.getFeedingRecordById(id);
        return ResponseEntity.ok(ApiResponse.<FeedingRecordDto>builder()
                .success(true)
                .data(feedingRecordMapper.toDto(record))
                .build());
    }

    @Operation(summary = "Crear un nuevo registro de alimentación")
    @PostMapping
    public ResponseEntity<ApiResponse<FeedingRecordDto>> createFeedingRecord(
            @PathVariable Long petId,
            @Valid @RequestBody FeedingRecordDto feedingRecordDto) {
        
        feedingRecordDto.setPetId(petId);
        FeedingRecord createdRecord = feedingRecordService.createFeedingRecord(feedingRecordDto);
        return new ResponseEntity<>(ApiResponse.<FeedingRecordDto>builder()
                .success(true)
                .message("Registro de alimentación creado correctamente")
                .data(feedingRecordMapper.toDto(createdRecord))
                .build(), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un registro de alimentación existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FeedingRecordDto>> updateFeedingRecord(
            @PathVariable Long petId,
            @PathVariable Long id,
            @Valid @RequestBody FeedingRecordDto feedingRecordDto) {
        
        feedingRecordDto.setPetId(petId);
        FeedingRecord updatedRecord = feedingRecordService.updateFeedingRecord(id, feedingRecordDto);
        return ResponseEntity.ok(ApiResponse.<FeedingRecordDto>builder()
                .success(true)
                .message("Registro de alimentación actualizado correctamente")
                .data(feedingRecordMapper.toDto(updatedRecord))
                .build());
    }

    @Operation(summary = "Eliminar un registro de alimentación")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFeedingRecord(
            @PathVariable Long petId,
            @PathVariable Long id) {
        
        feedingRecordService.deleteFeedingRecord(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Registro de alimentación eliminado correctamente")
                .build());
    }
}
