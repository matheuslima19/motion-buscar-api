package org.motion.buscar_api.domain.repositories.buscar;

import org.motion.buscar_api.domain.entities.Oficina;
import org.motion.buscar_api.domain.entities.buscar.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IAvaliacaoRepository extends JpaRepository<Avaliacao,Integer> {
    @Query("SELECT b.oficina.id, AVG(b.nota), COUNT(b.nota) " +
            "FROM Avaliacao b " +
            "GROUP BY b.oficina.id")
    List<Object[]> listarMediaNotaOficinas();

    List<Avaliacao> findAvaliacaoByOficina(Oficina oficina);
}
