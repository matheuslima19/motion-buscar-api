package org.motion.buscar_api.application.services;

import org.motion.buscar_api.application.dtos.AvaliacaoDTO.CreateAvaliacaoDTO;
import org.motion.buscar_api.application.dtos.AvaliacaoDTO.UpdateAvaliacaoDTO;
import org.motion.buscar_api.application.dtos.NotaOficinaDTO;
import org.motion.buscar_api.application.exception.RecursoNaoEncontradoException;
import org.motion.buscar_api.application.services.util.ServiceHelper;
import org.motion.buscar_api.domain.entities.Oficina;
import org.motion.buscar_api.domain.entities.buscar.Avaliacao;
import org.motion.buscar_api.domain.entities.buscar.Usuario;
import org.motion.buscar_api.domain.repositories.buscar.IAvaliacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvaliacaoService {

    @Autowired
    private IAvaliacaoRepository avaliacaoRepository;

    @Autowired
    private ServiceHelper serviceHelper;

    public List<Avaliacao> listarTodos() {
        return avaliacaoRepository.findAll();
    }

    public List<NotaOficinaDTO> listarMediaNotaOficinas() {

        List<Object[]> results = avaliacaoRepository.listarMediaNotaOficinas();

        return results.stream().map(result -> {
            NotaOficinaDTO dto = new NotaOficinaDTO();
            dto.setIdOficina((Integer) result[0]);
            dto.setNota((Double) result[1]);
            System.out.println(result[2]);
            dto.setQuantidadeAvaliacoes((Long) result[2]);
            return dto;
        }).collect(Collectors.toList());


    }

    public NotaOficinaDTO listarMediaNotaOficinaPorId(int id) {
        List<NotaOficinaDTO> medias = listarMediaNotaOficinas();
        serviceHelper.pegarOficinaValida(id);
        List<NotaOficinaDTO> mediaFiltrada = medias.stream().filter(notaOficinaDTO -> notaOficinaDTO.getIdOficina() == id).toList();

        return !mediaFiltrada.isEmpty() ? mediaFiltrada.get(0) : new NotaOficinaDTO(id,0.0, 0L);
    }


    public Avaliacao buscarPorId(int id) {
        return avaliacaoRepository.findById(id).orElseThrow(() ->
                new RecursoNaoEncontradoException("Avaliacao não encontrada com o id: " + id));
    }

    public List<Avaliacao> listarAvaliacoesPorOficina(int idOficina) {
        Oficina oficina = serviceHelper.pegarOficinaValida(idOficina);
        return avaliacaoRepository.findAvaliacaoByOficina(oficina);
    }

    public Avaliacao criar(CreateAvaliacaoDTO novaAvaliacao) {
        System.out.println(novaAvaliacao.fkOficina());
        Oficina oficina = serviceHelper.pegarOficinaValida(novaAvaliacao.fkOficina());
        Usuario usuario = serviceHelper.pegarUsuarioValido(novaAvaliacao.fkUsuario());
        Avaliacao avaliacao = new Avaliacao(novaAvaliacao, oficina, usuario);
        return avaliacaoRepository.save(avaliacao);
    }

    public void deletar(int id) {
        avaliacaoRepository.deleteById(id);
    }

    public Avaliacao atualizar(int id, UpdateAvaliacaoDTO avaliacaoAtualizada) {
        Avaliacao avaliacao = buscarPorId(id);
        avaliacao.setNota(avaliacaoAtualizada.nota());
        avaliacao.setComentario(avaliacaoAtualizada.comentario());
        return avaliacaoRepository.save(avaliacao);
    }
}
