package org.motion.buscar_api.domain.entities.buscar;

import jakarta.persistence.*;
import lombok.*;
import org.motion.buscar_api.domain.entities.Oficina;

@Table(name = "Buscar_OficinaFavorita")
@Entity(name = "OficinaFavorita")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OficinaFavorita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idOficinaFavorita;

    @ManyToOne @JoinColumn(name = "fkUsuario")
    private Usuario usuario;

    @ManyToOne @JoinColumn(name = "fkOficina")
    private Oficina oficina;


}
