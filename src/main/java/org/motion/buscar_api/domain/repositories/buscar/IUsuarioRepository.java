package org.motion.buscar_api.domain.repositories.buscar;

import org.motion.buscar_api.domain.entities.buscar.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface IUsuarioRepository extends JpaRepository<Usuario,Integer> {
    UserDetails findByEmail(String email);
    boolean existsByEmail(String email);

    Usuario findUsuarioByEmail(String email);

}
