package com.pethealthtracker.dto.pets;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.pethealthtracker.model.enums.Gender;
import com.pethealthtracker.model.enums.PetSpecies;
import com.pethealthtracker.model.enums.WeightUnit;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Value;

@Value
public class PetDTO {
    @NotBlank(message = "El nombre de la mascota no puede estar vacío")
    private String name;

    @NotNull(message = "La especie de la mascota es obligatoria")
    private PetSpecies species;

    private String breed;

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long userId;

    @PastOrPresent(message = "La fecha de nacimiento no puede ser futura")
    private LocalDate dateOfBirth;

    @DecimalMin(value = "0.01", message = "El peso debe ser positivo")
    private BigDecimal weight;

    private Integer ageYears;
    private Integer ageMonths;
    private WeightUnit weightUnit;
    private String profilePictureUrl;
    private String microchipNumber;
    private String color;
    private Gender gender;
    private String healthNotes;
}
