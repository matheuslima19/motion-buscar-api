package org.motion.buscar_api.domain.repositories;

import org.motion.buscar_api.domain.entities.Oficina;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOficinaRepository extends JpaRepository<Oficina,Integer> {
}
