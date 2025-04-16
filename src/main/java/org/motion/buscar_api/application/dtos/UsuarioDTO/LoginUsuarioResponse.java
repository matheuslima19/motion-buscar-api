package org.motion.buscar_api.application.dtos.UsuarioDTO;

public record LoginUsuarioResponse(
        Integer idUsuario,
        String email,
        String nome,
        String sobrenome,
        String token,
        String fotoUrl
) {
}
