package com.pethealthtracker.dto.veterinary_visits;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VeterinaryVisitRequestDTO {
    @Schema(description = "ID de la mascota que visita al veterinario", example = "1", required = true)
    @NotNull(message = "El ID de la mascota es obligatorio")
    private Long petId;

    @Schema(description = "Fecha y hora de la visita", example = "2023-06-15T10:30:00", required = true)
    @NotNull(message = "La fecha y hora de la visita son obligatorias")
    private LocalDateTime visitDateTime;

    @Schema(description = "Nombre del veterinario o clínica", example = "Clínica Veterinaria Central")
    @NotBlank(message = "El nombre del veterinario o clínica es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String veterinarianName;

    @Schema(description = "Razón de la visita", example = "Chequeo anual")
    @NotBlank(message = "La razón de la visita es obligatoria")
    @Size(max = 200, message = "La razón no puede exceder los 200 caracteres")
    private String reason;

    @Schema(description = "Diagnóstico realizado")
    private String diagnosis;

    @Schema(description = "Tratamiento prescrito")
    private String treatment;

    @Schema(description = "Medicamentos recetados")
    private String prescribedMedications;

    @Schema(description = "Próxima fecha de visita de seguimiento", example = "2023-09-15T10:30:00")
    private LocalDateTime nextVisitDate;

    @Schema(description = "Notas adicionales sobre la visita")
    private String notes;
}
