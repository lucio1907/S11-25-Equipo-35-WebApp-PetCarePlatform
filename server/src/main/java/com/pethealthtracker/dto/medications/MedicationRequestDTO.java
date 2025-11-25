package com.pethealthtracker.dto.medications;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class MedicationRequestDTO {
    @NotNull(message = "El ID de la mascota no puede ser nulo")
    private Long petId;

    @NotBlank(message = "El nombre del medicamento es obligatorio")
    private String medicationName;

    @Size(max = 100, message = "La dosis no puede exceder los 100 caracteres.")
    private String dosage;

    @Size(max = 100, message = "La frecuencia no puede exceder los 100 caracteres.")
    private String frequency;

    private LocalDate startDate;

    private LocalDate endDate;

    private String reason;

    private String administeringInstructions;

    @Schema(description = "Campo opcional. No necesario para enviar el body", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Boolean isActive;
}
