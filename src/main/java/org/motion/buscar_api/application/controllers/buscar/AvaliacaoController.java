package org.motion.buscar_api.application.controllers.buscar;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.motion.buscar_api.application.dtos.AvaliacaoDTO.CreateAvaliacaoDTO;
import org.motion.buscar_api.application.dtos.AvaliacaoDTO.UpdateAvaliacaoDTO;
import org.motion.buscar_api.application.dtos.NotaOficinaDTO;
import org.motion.buscar_api.application.services.AvaliacaoService;
import org.motion.buscar_api.domain.entities.buscar.Avaliacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
@SecurityRequirement(name = "motion_jwt")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @GetMapping
    public ResponseEntity<List<Avaliacao>> listarTodos() {
        List<Avaliacao> avaliacoes = avaliacaoService.listarTodos();
        if(avaliacoes.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(avaliacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avaliacao> buscarPorId(@PathVariable int id) {
        return ResponseEntity.ok(avaliacaoService.buscarPorId(id));
    }


    @GetMapping("/media-notas-oficinas")
    public ResponseEntity<List<NotaOficinaDTO>> listarMediaNotaOficinas() {
        List<NotaOficinaDTO> medias = avaliacaoService.listarMediaNotaOficinas();
        if(medias.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(medias);
    }

    @GetMapping("/media-notas-oficina/{id}")
    public ResponseEntity<NotaOficinaDTO> listarMediaNotaOficinaPorId(@PathVariable int id) {
        NotaOficinaDTO dto = avaliacaoService.listarMediaNotaOficinaPorId(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/oficina/{idOficina}")
    public ResponseEntity<List<Avaliacao>> listarAvaliacoesPorOficina(@PathVariable int idOficina) {
        List<Avaliacao> avaliacoes = avaliacaoService.listarAvaliacoesPorOficina(idOficina);
        if(avaliacoes.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(avaliacoes);
    }

    @PostMapping
    public ResponseEntity<Avaliacao> criar(@RequestBody @Valid CreateAvaliacaoDTO novaAvaliacao) {
        return ResponseEntity.ok(avaliacaoService.criar(novaAvaliacao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Avaliacao> atualizar(@PathVariable int id, @RequestBody UpdateAvaliacaoDTO avaliacaoAtualizada) {

        return ResponseEntity.ok(avaliacaoService.atualizar(id, avaliacaoAtualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) {
        avaliacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
