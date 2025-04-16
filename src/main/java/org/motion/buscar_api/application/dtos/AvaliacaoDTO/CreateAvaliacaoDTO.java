package org.motion.buscar_api.application.dtos.AvaliacaoDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record CreateAvaliacaoDTO(
        @NotNull @DecimalMin(value = "0.0") @DecimalMax(value = "5.0")
        Double nota,
        @NotBlank
        String comentario,
        @NotNull
        Integer fkUsuario,
        @NotNull
        Integer fkOficina
) {
}
