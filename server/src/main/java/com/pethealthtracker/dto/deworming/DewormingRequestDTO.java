package com.pethealthtracker.dto.deworming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DewormingRequestDTO {
    
    @NotNull(message = "Pet ID is required")
    private Long petId;
    
    @NotBlank(message = "Product name is required")
    private String productName;
    
    private String manufacturer;
    
    @NotNull(message = "Administration date is required")
    private LocalDate administrationDate;
    
    private LocalDate nextTreatmentDate;
    private String batchNumber;
    private String administeredBy;
    private String notes;
    
    @Builder.Default
    private Boolean isActive = true;
}
