package com.pethealthtracker.mapper;

import org.springframework.stereotype.Component;

import com.pethealthtracker.dto.medications.MedicationRequestDTO;
import com.pethealthtracker.dto.medications.MedicationResponseDTO;
import com.pethealthtracker.model.Medication;
import com.pethealthtracker.model.Pet;

@Component
public class MedicationMapper {
    public Medication toEntity(MedicationRequestDTO dto, Pet pet) {
        return Medication.builder()
                .pet(pet)
                .medicationName(dto.getMedicationName())
                .dosage(dto.getDosage())
                .frequency(dto.getFrequency())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .reason(dto.getReason())
                .administeringInstructions(dto.getAdministeringInstructions())
                .isActive(dto.getIsActive() == null ? true : dto.getIsActive())
                .build();
    }

    public MedicationResponseDTO toResponseDTO(Medication medication) {
        return MedicationResponseDTO.builder()
                .id(medication.getId())
                .petId(medication.getPet().getId())
                .medicationName(medication.getMedicationName())
                .dosage(medication.getDosage())
                .frequency(medication.getFrequency())
                .startDate(medication.getStartDate())
                .endDate(medication.getEndDate())
                .reason(medication.getReason())
                .administeringInstructions(medication.getAdministeringInstructions())
                .isActive(medication.isActive())
                .createdAt(medication.getCreatedAt())
                .build();
    }
}
