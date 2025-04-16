package org.motion.buscar_api.application.services.util;

import org.motion.buscar_api.application.exception.RecursoNaoEncontradoException;
import org.motion.buscar_api.domain.entities.Oficina;
import org.motion.buscar_api.domain.entities.buscar.Usuario;
import org.motion.buscar_api.domain.repositories.IOficinaRepository;
import org.motion.buscar_api.domain.repositories.buscar.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceHelper {

    @Autowired
    private IOficinaRepository oficinaRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;


    /**
     * @param id
     * @return Retorna uma oficina caso encontre ou uma exceção caso não.
     * @throws RecursoNaoEncontradoException
     */
    public Oficina pegarOficinaValida(int id) {
        return oficinaRepository.findById(id).orElseThrow(() ->
                new RecursoNaoEncontradoException("Oficina não encontrada com o id: " + id));

    }

    /**
     * @param id
     * @return Retorna um usuario caso encontre ou uma exceção caso não.
     * @throws RecursoNaoEncontradoException
     */
    public Usuario pegarUsuarioValido(int id){
        return usuarioRepository.findById(id).orElseThrow(() ->
                        new RecursoNaoEncontradoException("Usuario não encontrado com o id: " + id));
    }
}
