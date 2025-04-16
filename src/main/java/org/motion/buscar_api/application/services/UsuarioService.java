package org.motion.buscar_api.application.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.motion.buscar_api.application.dtos.UsuarioDTO.*;
import org.motion.buscar_api.application.exception.DadoUnicoDuplicadoException;
import org.motion.buscar_api.application.exception.RecursoNaoEncontradoException;
import org.motion.buscar_api.application.exception.SenhaIncorretaException;
import org.motion.buscar_api.application.exception.UsuarioGoogleExistenteException;
import org.motion.buscar_api.domain.entities.buscar.Usuario;
import org.motion.buscar_api.domain.repositories.buscar.IUsuarioRepository;
import org.motion.buscar_api.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(int id) {
        return usuarioRepository.findById(id).orElseThrow(() ->
                new RecursoNaoEncontradoException("Usuario não encontrado com o id: " + id));
    }

    public UserDetails buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario criar(CreateUsuarioDTO novoUsuarioDTO) {
        verificarEmailDuplicado(novoUsuarioDTO.email());
        Usuario usuario = new Usuario(novoUsuarioDTO);
        usuario.setPreferenciasPropulsao(novoUsuarioDTO.preferenciasPropulsao());
        usuario.setPreferenciasVeiculo(novoUsuarioDTO.preferenciasVeiculos());
        String senhaCriptografada = new BCryptPasswordEncoder().encode(novoUsuarioDTO.senha());
        usuario.setSenha(senhaCriptografada);
        return usuarioRepository.save(usuario);
    }

    public LoginUsuarioResponse login(@Valid LoginUsuarioRequest request){
        Usuario usuario = (Usuario) buscarPorEmail(request.email());
        if (usuario == null) {
            throw new RecursoNaoEncontradoException("Essa conta Buscar não existe. Insira uma conta diferente ou obtenha uma nova.");
        }

        if (usuario.getGoogleSub() != null) {
            throw new UsuarioGoogleExistenteException("Essa conta foi criada com o Google. Faça login com o Google.");
        }

        if (!new BCryptPasswordEncoder().matches(request.senha(), usuario.getSenha())) {
            throw new SenhaIncorretaException("Sua conta ou senha está incorreta.");
        }

        var usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.senha());
        var auth = authenticationManager.authenticate(usernamePassword);
        String token = tokenService.generateToken((Usuario) auth.getPrincipal());
        return new LoginUsuarioResponse(usuario.getIdUsuario(),usuario.getEmail(),usuario.getNome(),usuario.getSobrenome(),token,usuario.getFotoUrl());
    }

    @Transactional
    public Usuario atualizar(int id, UpdateUsuarioDTO updateUsuarioDTO) {
        Usuario usuario = buscarPorId(id);
        usuario.setNome(updateUsuarioDTO.getNome());
        usuario.setSobrenome(updateUsuarioDTO.getSobrenome());
        usuario.setEmail(updateUsuarioDTO.getEmail());
        return usuarioRepository.save(usuario);
    }


    @Transactional
    public Usuario atualizarSenha(int id, UpdateSenhaUsuarioDTO updateSenhaUsuarioDTO) {
        Usuario usuario = buscarPorId(id);
        boolean isOldPasswordCorrect = new BCryptPasswordEncoder().matches(updateSenhaUsuarioDTO.getSenhaAntiga(), usuario.getSenha());
        if(!isOldPasswordCorrect) throw new SenhaIncorretaException("Senha incorreta.");
        String senhaCriptografada = new BCryptPasswordEncoder().encode(updateSenhaUsuarioDTO.getSenhaNova());
        usuario.setSenha(senhaCriptografada);
        return usuarioRepository.save(usuario);
    }

    public void enviarTokenConfirmacao(String email, String op) throws MessagingException {
        Usuario usuario = usuarioRepository.findUsuarioByEmail(email);
        if (usuario == null) throw new RecursoNaoEncontradoException("Essa conta Buscar não existe. Insira uma conta diferente ou obtenha uma nova.");

        String token = geradorDeSenhaAleatoria();
        usuario.setConfirmToken(token);
        usuarioRepository.save(usuario);

        if (op.equalsIgnoreCase("senha"))
            emailRecuperacao(usuario);
        else if (op.equalsIgnoreCase("email")) {
            emailTokenConfirmacao(email, token);
        }
    }

    public void validarTokenConfirmacao(ConfirmTokenDTO dto, String op) {
        Usuario usuario = usuarioRepository.findUsuarioByEmail(dto.getEmail());
        if (usuario == null) throw new RecursoNaoEncontradoException("Email não encontrado no sistema");
        if (usuario.getConfirmToken() == null) throw new RecursoNaoEncontradoException("Token não foi gerado");
        if (!usuario.getConfirmToken().equalsIgnoreCase(dto.getToken()))
            throw new SenhaIncorretaException("Token inválido");

        usuario.setConfirmToken(null);
        if(op.equalsIgnoreCase("senha")){
            String senhaCriptografada = new BCryptPasswordEncoder().encode(dto.getSenha());
            usuario.setSenha(senhaCriptografada);
        }
        usuarioRepository.save(usuario);
    }


   public void deletar(int id){
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
   }

   public Usuario atualizarFoto(int id, UpdateFotoUsuarioDTO updateFotoUsuarioDTO){
        Usuario usuario = buscarPorId(id);
        usuario.setFotoUrl(updateFotoUsuarioDTO.getFotoUrl());
        return usuarioRepository.save(usuario);
    }

    private void verificarEmailDuplicado(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new DadoUnicoDuplicadoException("Email já cadastrado. Vá para login");
        }
    }

    private void emailRecuperacao(Usuario usuario) throws MessagingException {

        String htmlTemplate = "<html lang=\"en\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <link href='https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap' rel='stylesheet'>" +
                "</head>" +
                "<body style=\"font-family: Roboto,sans-serif;\">" +
                "    <h2>Olá, %s.</h2><br>" +
                "    <span>Insira este código para concluir a redefinição</span><br>" +
                "    <h1>%s</h1>" +
                "    <span>Se você não solicitou esse código, recomendamos que altere sua senha.</span>" +
                "</body>" +
                "</html>";

        String htmlContent = String.format(htmlTemplate, usuario.getNome(), usuario.getConfirmToken());


        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(usuario.getEmail());
        helper.setSubject("Recuperação de senha");
        helper.setText(htmlContent, true);
        emailSender.send(message);
    }


    private void emailTokenConfirmacao(String email, String token) throws MessagingException {
        String htmlBody = "<html>" +
                "<head><link href='https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap' rel='stylesheet'></head>" +
                "<body style='font-family: Roboto, sans-serif;'><h2>Confirme seu email utilizando o token abaixo.<br></h2>" +
                "<p>Token de confirmação: <strong>" + token + "</strong></p>" +
                "<p>Atenciosamente,</p>" +
                "<p>A equipe motion</p>" +
                "</body></html>";


        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(email);
        helper.setSubject("Confirme seu email motion");
        helper.setText(htmlBody, true); // Habilita o processamento de HTML
        emailSender.send(message);
    }

    private String geradorDeSenhaAleatoria() {
        final String CARACTERES_PERMITIDOS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            int indice = random.nextInt(CARACTERES_PERMITIDOS.length());
            sb.append(CARACTERES_PERMITIDOS.charAt(indice));
        }
        return sb.toString();
    }

    public Usuario criarUsuarioGoogle(CreateUserGoogleDTO novoUsuarioDTO) {
        verificarEmailDuplicado(novoUsuarioDTO.email());
        Usuario usuario = new Usuario(novoUsuarioDTO);
        String senhaCriptografada = new BCryptPasswordEncoder().encode(novoUsuarioDTO.googleSub());
        usuario.setSenha(senhaCriptografada);
        return usuarioRepository.save(usuario);
    }

    public GoogleResponseDTO loginGoogle(GoogleAuthDTO googleAuthDTO) {
        Usuario usuario = (Usuario) buscarPorEmail(googleAuthDTO.email());
        if (usuario == null) {
            throw new RecursoNaoEncontradoException("Essa conta Buscar não existe. Insira uma conta diferente ou obtenha uma nova.");
        }
        var usernamePassword = new UsernamePasswordAuthenticationToken(googleAuthDTO.email(), googleAuthDTO.googleSub());
        var auth = authenticationManager.authenticate(usernamePassword);
        String token = tokenService.generateToken((Usuario) auth.getPrincipal());
        return new GoogleResponseDTO(usuario.getIdUsuario(),usuario.getNome(), token, usuario.getFotoUrl());
    }

    public Usuario atualizarPreferencias(int id, UpdatePreferenciasDTO dto){
        Usuario usuario = buscarPorId(id);
        usuario.setPreferenciasPropulsao(dto.getPreferenciasPropulsao());
        usuario.setPreferenciasVeiculo(dto.getPreferenciasVeiculos());
        return usuarioRepository.save(usuario);
    }
}
