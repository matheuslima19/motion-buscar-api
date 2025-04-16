package org.motion.buscar_api.application.services;

import org.motion.buscar_api.domain.entities.Oficina;
import org.motion.buscar_api.domain.entities.buscar.OficinaFavorita;
import org.motion.buscar_api.domain.entities.buscar.Usuario;
import org.motion.buscar_api.domain.repositories.IOficinaRepository;
import org.motion.buscar_api.domain.repositories.buscar.IOficinaFavoritaRepository;
import org.motion.buscar_api.domain.repositories.buscar.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OficinaFavoritaService {

    @Autowired
    private IOficinaFavoritaRepository oficinaFavoritaRepository;

    @Autowired
    IOficinaRepository oficinaRepository;

    @Autowired
    IUsuarioRepository usuarioRepository;

    public OficinaFavorita salvarOficinaFavorita(Integer idUsuario, Integer idOficina) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow();
        Oficina oficina = oficinaRepository.findById(idOficina).orElseThrow();
        OficinaFavorita oficinaFavorita = new OficinaFavorita();
        oficinaFavorita.setUsuario(usuario);
        oficinaFavorita.setOficina(oficina);
        return oficinaFavoritaRepository.save(oficinaFavorita);
    }

    public List<OficinaFavorita> listarOficinasFavoritas(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow();
        return oficinaFavoritaRepository.findByUsuario(usuario);
    }

    public void desfavoritarOficina(Integer idUsuario, Integer idOficina){
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow();
        Oficina oficina = oficinaRepository.findById(idOficina).orElseThrow();

        OficinaFavorita oficinasFavoritas = oficinaFavoritaRepository.findByUsuarioAndOficina(usuario, oficina);
        oficinaFavoritaRepository.delete(oficinasFavoritas);
    }
}
