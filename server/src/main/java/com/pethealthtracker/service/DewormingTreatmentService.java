package com.pethealthtracker.service;

import com.pethealthtracker.dto.deworming.DewormingRequestDTO;
import com.pethealthtracker.dto.deworming.DewormingResponseDTO;
import com.pethealthtracker.exception.ResourceNotFoundException;
import com.pethealthtracker.model.DewormingTreatment;
import com.pethealthtracker.model.Pet;
import com.pethealthtracker.repository.DewormingTreatmentRepository;
import com.pethealthtracker.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DewormingTreatmentService {

    private final DewormingTreatmentRepository dewormingTreatmentRepository;
    private final PetRepository petRepository;

    @Transactional
    public DewormingResponseDTO createDewormingTreatment(DewormingRequestDTO requestDTO) {
        // Validate pet exists
        Pet pet = petRepository.findById(requestDTO.getPetId())
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));

        // Map DTO to entity
        DewormingTreatment treatment = mapToEntity(requestDTO, pet);

        // Save to database
        DewormingTreatment savedTreatment = dewormingTreatmentRepository.save(treatment);

        // Return response DTO
        return mapToResponseDTO(savedTreatment);
    }

    @Transactional(readOnly = true)
    public List<DewormingResponseDTO> getDewormingTreatmentsByPetId(Long petId) {
        // Validate pet exists
        if (!petRepository.existsById(petId)) {
            throw new ResourceNotFoundException("Pet not found");
        }

        List<DewormingTreatment> treatments = dewormingTreatmentRepository.findByPetId(petId);
        return treatments.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DewormingResponseDTO getDewormingTreatmentById(Long id) {
        DewormingTreatment treatment = dewormingTreatmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deworming treatment not found"));
        return mapToResponseDTO(treatment);
    }

    @Transactional
    public DewormingResponseDTO updateDewormingTreatment(Long id, DewormingRequestDTO requestDTO) {
        DewormingTreatment existingTreatment = dewormingTreatmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deworming treatment not found"));

        // Update fields
        existingTreatment.setProductName(requestDTO.getProductName());
        existingTreatment.setManufacturer(requestDTO.getManufacturer());
        existingTreatment.setAdministrationDate(requestDTO.getAdministrationDate());
        existingTreatment.setNextTreatmentDate(requestDTO.getNextTreatmentDate());
        existingTreatment.setBatchNumber(requestDTO.getBatchNumber());
        existingTreatment.setAdministeredBy(requestDTO.getAdministeredBy());
        existingTreatment.setNotes(requestDTO.getNotes());
        
        if (requestDTO.getIsActive() != null) {
            existingTreatment.setIsActive(requestDTO.getIsActive());
        }

        DewormingTreatment updatedTreatment = dewormingTreatmentRepository.save(existingTreatment);
        return mapToResponseDTO(updatedTreatment);
    }

    @Transactional
    public void deleteDewormingTreatment(Long id) {
        DewormingTreatment treatment = dewormingTreatmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deworming treatment not found"));
        
        // Soft delete
        treatment.setIsActive(false);
        dewormingTreatmentRepository.save(treatment);
    }

    // =========================================================
    // Private Mapper Methods
    // =========================================================

    private DewormingTreatment mapToEntity(DewormingRequestDTO dto, Pet pet) {
        return DewormingTreatment.builder()
                .pet(pet)
                .productName(dto.getProductName())
                .manufacturer(dto.getManufacturer())
                .administrationDate(dto.getAdministrationDate())
                .nextTreatmentDate(dto.getNextTreatmentDate())
                .batchNumber(dto.getBatchNumber())
                .administeredBy(dto.getAdministeredBy())
                .notes(dto.getNotes())
                .isActive(dto.getIsActive() == null || dto.getIsActive())
                .build();
    }

    private DewormingResponseDTO mapToResponseDTO(DewormingTreatment treatment) {
        return DewormingResponseDTO.builder()
                .id(treatment.getId())
                .petId(treatment.getPet().getId())
                .productName(treatment.getProductName())
                .manufacturer(treatment.getManufacturer())
                .administrationDate(treatment.getAdministrationDate())
                .nextTreatmentDate(treatment.getNextTreatmentDate())
                .batchNumber(treatment.getBatchNumber())
                .administeredBy(treatment.getAdministeredBy())
                .notes(treatment.getNotes())
                .isActive(treatment.getIsActive())
                .createdAt(treatment.getCreatedAt())
                .build();
    }
}
