package org.motion.buscar_api.application.dtos.AvaliacaoDTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateAvaliacaoDTO(
        @NotNull @DecimalMin(value = "0.0") @DecimalMax(value = "5.0")
        Double nota,
        @NotBlank
        String comentario
) {
}
