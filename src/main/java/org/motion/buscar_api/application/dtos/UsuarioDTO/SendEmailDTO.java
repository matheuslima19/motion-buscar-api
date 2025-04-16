package org.motion.buscar_api.application.dtos.UsuarioDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendEmailDTO {
    @NotBlank
    String email;
}
