package org.motion.buscar_api.application.dtos.UsuarioDTO;

public record CreateUserGoogleDTO(
    String email,
    String googleSub,
    String nome,
    String sobrenome,
    String fotoUrl
) {
}
