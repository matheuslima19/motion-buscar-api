package org.motion.buscar_api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.motion.buscar_api.domain.entities.buscar.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${JWT_SECRET:my-secret}")
    private String secret;

    public String generateToken(Usuario usuario) {

        var algoritmo = Algorithm.HMAC256(secret);
        String token = JWT
                .create()
                .withIssuer("motion-api")
                .withSubject(usuario.getEmail())
                .withExpiresAt(LocalDateTime.now().plusDays(30).toInstant(ZoneOffset.of("-03:00")))
                //.withExpiresAt(LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.UTC)))
                .sign(algoritmo);
        return token;


    }

    public String validateToken(String token){
        var algoritmo = Algorithm.HMAC256(secret);
        return JWT.require(algoritmo)
                .withIssuer("motion-api")
                .build()
                .verify(token)
                .getSubject();
    }

}
