package com.pethealthtracker.dto.deworming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DewormingResponseDTO {
    
    private Long id;
    private Long petId;
    private String productName;
    private String manufacturer;
    private LocalDate administrationDate;
    private LocalDate nextTreatmentDate;
    private String batchNumber;
    private String administeredBy;
    private String notes;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
