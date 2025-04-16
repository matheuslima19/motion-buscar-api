package org.motion.buscar_api.application.exception;

public class UsuarioGoogleExistenteException extends RuntimeException {
    public UsuarioGoogleExistenteException(String message) {
        super(message);
    }
}
