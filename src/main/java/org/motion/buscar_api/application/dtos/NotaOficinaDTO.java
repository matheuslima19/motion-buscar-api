package org.motion.buscar_api.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotaOficinaDTO {
    private Integer idOficina;
    private Double nota;
    private Long quantidadeAvaliacoes;
}
