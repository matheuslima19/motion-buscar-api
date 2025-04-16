package org.motion.buscar_api.application.dtos.UsuarioDTO;

import jakarta.validation.constraints.NotBlank;

public record LoginUsuarioRequest(
        @NotBlank
        String email,
        @NotBlank
        String senha
) {
}
