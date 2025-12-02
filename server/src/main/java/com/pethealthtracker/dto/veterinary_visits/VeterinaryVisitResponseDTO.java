package com.pethealthtracker.dto.veterinary_visits;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VeterinaryVisitResponseDTO {
    @Schema(description = "ID del registro de la visita veterinaria", example = "1")
    private Long id;

    @Schema(description = "ID de la mascota que visitó al veterinario", example = "1")
    private Long petId;

    @Schema(description = "Nombre de la mascota")
    private String petName;

    @Schema(description = "Fecha y hora de la visita", example = "2023-06-15T10:30:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime visitDateTime;

    @Schema(description = "Nombre del veterinario o clínica", example = "Clínica Veterinaria Central")
    private String veterinarianName;

    @Schema(description = "Razón de la visita", example = "Chequeo anual")
    private String reason;

    @Schema(description = "Diagnóstico realizado")
    private String diagnosis;

    @Schema(description = "Tratamiento prescrito")
    private String treatment;

    @Schema(description = "Medicamentos recetados")
    private String prescribedMedications;

    @Schema(description = "Próxima fecha de visita de seguimiento", example = "2023-09-15T10:30:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime nextVisitDate;

    @Schema(description = "Notas adicionales sobre la visita")
    private String notes;

    @Schema(description = "Indica si el registro está activo", example = "true")
    private Boolean isActive;

    @Schema(description = "Fecha de creación del registro", example = "2023-06-15T12:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de la última actualización", example = "2023-06-15T14:30:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
