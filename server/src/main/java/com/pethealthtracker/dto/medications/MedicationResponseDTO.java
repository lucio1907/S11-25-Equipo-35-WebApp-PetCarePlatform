package com.pethealthtracker.dto.medications;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicationResponseDTO {
    private Long id;
    private Long petId;
    private String medicationName;
    private String dosage;
    private String frequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String administeringInstructions;
    private Boolean isActive;
    private LocalDate createdAt;
}
