package org.motion.buscar_api.domain.repositories.buscar;

import org.motion.buscar_api.domain.entities.Oficina;
import org.motion.buscar_api.domain.entities.buscar.OficinaFavorita;
import org.motion.buscar_api.domain.entities.buscar.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOficinaFavoritaRepository extends JpaRepository<OficinaFavorita, Integer> {
    List<OficinaFavorita> findByUsuario (Usuario usuario);
    OficinaFavorita findByUsuarioAndOficina(Usuario usuario, Oficina oficina);
}
