package org.motion.buscar_api.application.dtos.UsuarioDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConfirmTokenDTO {
    @NotBlank
    private String email;
    private String senha;
    @NotBlank
    private String token;
}
