package com.pethealthtracker.controller;

import com.pethealthtracker.dto.deworming.DewormingRequestDTO;
import com.pethealthtracker.dto.deworming.DewormingResponseDTO;
import com.pethealthtracker.service.DewormingTreatmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets/{petId}/deworming-treatments")
@RequiredArgsConstructor
public class DewormingTreatmentController {

    private final DewormingTreatmentService dewormingTreatmentService;

    @PostMapping
    public ResponseEntity<DewormingResponseDTO> createDewormingTreatment(
            @PathVariable Long petId,
            @Valid @RequestBody DewormingRequestDTO requestDTO) {
        
        requestDTO.setPetId(petId);
        DewormingResponseDTO response = dewormingTreatmentService.createDewormingTreatment(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DewormingResponseDTO>> getDewormingTreatmentsByPetId(
            @PathVariable Long petId) {
        
        List<DewormingResponseDTO> treatments = dewormingTreatmentService.getDewormingTreatmentsByPetId(petId);
        return ResponseEntity.ok(treatments);
    }

    @GetMapping("/{treatmentId}")
    public ResponseEntity<DewormingResponseDTO> getDewormingTreatmentById(
            @PathVariable Long petId,
            @PathVariable Long treatmentId) {
        
        DewormingResponseDTO treatment = dewormingTreatmentService.getDewormingTreatmentById(treatmentId);
        return ResponseEntity.ok(treatment);
    }

    @PutMapping("/{treatmentId}")
    public ResponseEntity<DewormingResponseDTO> updateDewormingTreatment(
            @PathVariable Long petId,
            @PathVariable Long treatmentId,
            @Valid @RequestBody DewormingRequestDTO requestDTO) {
        
        requestDTO.setPetId(petId);
        DewormingResponseDTO updatedTreatment = dewormingTreatmentService.updateDewormingTreatment(treatmentId, requestDTO);
        return ResponseEntity.ok(updatedTreatment);
    }

    @DeleteMapping("/{treatmentId}")
    public ResponseEntity<Void> deleteDewormingTreatment(
            @PathVariable Long petId,
            @PathVariable Long treatmentId) {
        
        dewormingTreatmentService.deleteDewormingTreatment(treatmentId);
        return ResponseEntity.noContent().build();
    }
}
