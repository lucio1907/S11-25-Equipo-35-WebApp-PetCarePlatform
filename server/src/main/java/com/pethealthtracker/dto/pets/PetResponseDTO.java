package com.pethealthtracker.dto.pets;

import com.pethealthtracker.model.enums.Gender;
import com.pethealthtracker.model.enums.PetSpecies;
import com.pethealthtracker.model.enums.WeightUnit;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PetResponseDTO {
    
    // Campos generados por el sistema/ID
    private Long id;
    private Long userId;

    // Datos principales
    private String name;
    private PetSpecies species;
    private String breed;
    private LocalDate dateOfBirth;
    
    // Campos calculados por el servicio
    private Integer ageYears;
    private Integer ageMonths;

    private BigDecimal weight;
    private WeightUnit weightUnit;
    private String profilePictureUrl;
    private String microchipNumber;
    private String color;
    private Gender gender;
    private String healthNotes;
    private Boolean isActive; // Estado lógico de eliminación

    // Campos de auditoría (timestamps)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}