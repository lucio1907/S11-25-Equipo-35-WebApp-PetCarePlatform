package com.pethealthtracker.dto.vaccinations;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationRequestDTO {
    @Schema(description = "ID de la mascota a la que se le aplica la vacuna", example = "1", required = true)
    @NotNull(message = "El ID de la mascota es obligatorio")
    private Long petId;

    @Schema(description = "Nombre de la vacuna", example = "Rabia", required = true)
    @NotBlank(message = "El nombre de la vacuna es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String vaccineName;

    @Schema(description = "Fabricante de la vacuna", example = "Zoetis")
    @Size(max = 100, message = "El fabricante no puede exceder los 100 caracteres")
    private String manufacturer;

    @Schema(description = "Número de lote de la vacuna", example = "LOT12345")
    @Size(max = 50, message = "El número de lote no puede exceder los 50 caracteres")
    private String batchNumber;

    @Schema(description = "Fecha de administración de la vacuna (formato: yyyy-MM-dd)", example = "2023-01-15", required = true)
    @NotNull(message = "La fecha de administración es obligatoria")
    private LocalDate administrationDate;

    @Schema(description = "Fecha de vencimiento de la vacuna (formato: yyyy-MM-dd)", example = "2024-01-15")
    private LocalDate expirationDate;

    @Schema(description = "Fecha sugerida para la próxima dosis (formato: yyyy-MM-dd)", example = "2024-01-15")
    private LocalDate nextDoseDate;

    @Schema(description = "Nombre del profesional que administró la vacuna", example = "Dr. Juan Pérez")
    @Size(max = 100, message = "El nombre del administrador no puede exceder los 100 caracteres")
    private String administeredBy;

    @Schema(description = "Notas adicionales sobre la vacunación")
    private String notes;
}
