package com.pethealthtracker.dto.vaccinations;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationResponseDTO {
    @Schema(description = "ID del registro de vacunación", example = "1")
    private Long id;

    @Schema(description = "ID de la mascota vacunada", example = "1")
    private Long petId;

    @Schema(description = "Nombre de la mascota", example = "Firulais")
    private String petName;

    @Schema(description = "Nombre de la vacuna", example = "Rabia")
    private String vaccineName;

    @Schema(description = "Fabricante de la vacuna", example = "Zoetis")
    private String manufacturer;

    @Schema(description = "Número de lote de la vacuna", example = "LOT12345")
    private String batchNumber;

    @Schema(description = "Fecha de administración de la vacuna", example = "2023-01-15", format = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate administrationDate;

    @Schema(description = "Fecha de vencimiento de la vacuna", example = "2024-01-15", format = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;

    @Schema(description = "Fecha sugerida para la próxima dosis", example = "2024-01-15", format = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate nextDoseDate;

    @Schema(description = "Nombre del profesional que administró la vacuna", example = "Dr. Juan Pérez")
    private String administeredBy;

    @Schema(description = "Notas adicionales sobre la vacunación")
    private String notes;

    @Schema(description = "Indica si el registro está activo", example = "true")
    private Boolean isActive;

    @Schema(description = "Fecha de creación del registro", example = "2023-01-15")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
}
