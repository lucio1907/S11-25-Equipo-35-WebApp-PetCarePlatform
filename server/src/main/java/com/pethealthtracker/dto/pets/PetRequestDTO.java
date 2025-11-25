package com.pethealthtracker.dto.pets;

import com.pethealthtracker.model.enums.Gender;
import com.pethealthtracker.model.enums.PetSpecies;
import com.pethealthtracker.model.enums.WeightUnit;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class PetRequestDTO {
    
    @NotBlank(message = "El nombre de la mascota no puede estar vacío.")
    @Size(max = 100)
    private String name;

    @NotNull(message = "La especie es obligatoria.")
    private PetSpecies species;

    @Size(max = 100)
    private String breed;

    @PastOrPresent(message = "La fecha de nacimiento no puede ser futura.")
    private LocalDate dateOfBirth;

    // Los campos de edad (ageYears, ageMonths) generalmente NO se incluyen
    // en el DTO de petición si se calculan a partir de dateOfBirth.
    
    @DecimalMin(value = "0.01", message = "El peso debe ser positivo.")
    @Digits(integer = 3, fraction = 2, message = "El peso debe tener hasta 3 dígitos enteros y 2 decimales.")
    private BigDecimal weight;

    private WeightUnit weightUnit;
    
    @Size(max = 500)
    private String profilePictureUrl;

    @Size(max = 100)
    private String microchipNumber;

    @Size(max = 100)
    private String color;

    private Gender gender;

    // TEXT se mapea a String
    private String healthNotes;
}