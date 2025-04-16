package org.motion.buscar_api.application.dtos.UsuarioDTO;

import jakarta.validation.constraints.NotBlank;

public record CreateUsuarioDTO(
        @NotBlank
        String nome,
        @NotBlank
        String sobrenome,
        @NotBlank
        String email,
        @NotBlank
        String senha,
        String preferenciasVeiculos,
        String preferenciasPropulsao
) {
}
