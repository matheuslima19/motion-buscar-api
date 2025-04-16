package org.motion.buscar_api.application.controllers.buscar;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.PermitAll;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.motion.buscar_api.application.dtos.UsuarioDTO.*;
import org.motion.buscar_api.application.services.UsuarioService;
import org.motion.buscar_api.domain.entities.buscar.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "motion_jwt")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping()
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.status(200).body(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable int id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Usuario> cadastrar(@RequestBody @Valid CreateUsuarioDTO usuario) {
        Usuario usuarioCadastrado = usuarioService.criar(usuario);
        return ResponseEntity.status(201).body(usuarioCadastrado);
    }

    @PostMapping("/login") @PermitAll
    public ResponseEntity<LoginUsuarioResponse> login(@RequestBody @Valid LoginUsuarioRequest usuario) {
        LoginUsuarioResponse usuarioLogado = usuarioService.login(usuario);
        return ResponseEntity.status(200).body(usuarioLogado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable int id, @RequestBody @Valid UpdateUsuarioDTO updateUsuarioDTO) {
        Usuario usuarioAtualizado = usuarioService.atualizar(id,updateUsuarioDTO);
        return ResponseEntity.status(200).body(usuarioAtualizado);
    }

    @Operation(summary = "Seta um token de um gerente pelo email e envia o email com o token. Recebe um parâmetro op que pode ser email ou senha para a respectiva operação")
    @PostMapping("/set-token")
    public ResponseEntity<Void> setToken(@RequestBody @Valid SendEmailDTO dto, @RequestParam String op) throws MessagingException {
        usuarioService.enviarTokenConfirmacao(dto.getEmail(), op);
        return ResponseEntity.status(204).build();
    }
    @Operation(summary = "Confirma o token de um gerente, após a confirmação o token é removido. Recebe um parâmetro op que pode ser email ou senha para a respectiva operação. Caso a operação seja confirmação de email não é necessário colocar a nova senha")
    @PostMapping("/confirmar-token")
    public ResponseEntity<Void> confirmarToken(@RequestBody @Valid ConfirmTokenDTO dto, @RequestParam String op) {
        usuarioService.validarTokenConfirmacao(dto,op);
        return ResponseEntity.status(204).build();
    }


    @PutMapping("/atualizar-senha/{id}")
    public ResponseEntity<Usuario> atualizarSenha(@PathVariable int id, @RequestBody UpdateSenhaUsuarioDTO updateSenhaUsuarioDTO) {
        Usuario usuarioAtualizado = usuarioService.atualizarSenha(id,updateSenhaUsuarioDTO);
        return ResponseEntity.status(200).body(usuarioAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable int id) {
        usuarioService.deletar(id);
        return ResponseEntity.status(200).body("Usuario deletado com sucesso");
    }

    @PutMapping("/atualizar-foto/{id}")
    public ResponseEntity<Usuario> atualizarFoto(@PathVariable int id, @RequestBody UpdateFotoUsuarioDTO updateFotoUsuarioDTO) {
        Usuario usuarioAtualizado = usuarioService.atualizarFoto(id,updateFotoUsuarioDTO);
        return ResponseEntity.status(200).body(usuarioAtualizado);
    }

    @PostMapping("/login-google")
    public ResponseEntity loginGoogle(@RequestBody @Valid GoogleAuthDTO googleAuthDTO) {
        GoogleResponseDTO googleResponseDTO = usuarioService.loginGoogle(googleAuthDTO);
        return googleResponseDTO.token().isBlank() ? ResponseEntity.badRequest().build() : ResponseEntity.ok(googleResponseDTO);
    }

    @PostMapping("/cadastrar-google")
    public ResponseEntity<Usuario> cadastrarGoogle(@RequestBody @Valid CreateUserGoogleDTO googleUser) {
        Usuario usuarioCadastrado = usuarioService.criarUsuarioGoogle(googleUser);
        return ResponseEntity.status(201).body(usuarioCadastrado);
    }

    @PutMapping("/atualizar-preferencias/{id}")
    public ResponseEntity<Usuario> atualizarPreferencias(@PathVariable int id, @RequestBody UpdatePreferenciasDTO dto) {
        Usuario usuarioAtualizado = usuarioService.atualizarPreferencias(id,dto);
        return ResponseEntity.status(200).body(usuarioAtualizado);
    }
}
