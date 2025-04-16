package org.motion.buscar_api.application.controllers.buscar;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.motion.buscar_api.application.services.OficinaFavoritaService;
import org.motion.buscar_api.domain.entities.buscar.OficinaFavorita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios/{idUsuario}/oficinas-favoritas")
@SecurityRequirement(name = "motion_jwt")
public class OficinaFavoritaController {

    @Autowired
    private OficinaFavoritaService oficinaFavoritaService;

    @GetMapping()
    public ResponseEntity<List<OficinaFavorita>> listarOficinasFavoritas(@PathVariable int idUsuario) {
        List<OficinaFavorita> oficinasFavoritas = oficinaFavoritaService.listarOficinasFavoritas(idUsuario);
        return ResponseEntity.status(200).body(oficinasFavoritas);
    }

    @PostMapping("/{idOficina}")
    public ResponseEntity<OficinaFavorita> salvarOficinaFavorita(@PathVariable int idUsuario, @PathVariable int idOficina) {
        OficinaFavorita oficinaFavorita = oficinaFavoritaService.salvarOficinaFavorita(idUsuario, idOficina);
        return ResponseEntity.status(201).body(oficinaFavorita);
    }

    @DeleteMapping("/{idOficina}")
    public ResponseEntity<Void> deletarOficinaFavorita(@PathVariable int idUsuario, @PathVariable int idOficina){
        oficinaFavoritaService.desfavoritarOficina(idUsuario, idOficina);
        return ResponseEntity.status(204).build();
    }
}