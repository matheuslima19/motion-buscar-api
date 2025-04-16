package org.motion.buscar_api.application.dtos.UsuarioDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateSenhaUsuarioDTO {
    @NotBlank
    private String senhaAntiga;
    @NotBlank
    private String senhaNova;
}
