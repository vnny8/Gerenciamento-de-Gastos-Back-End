package com.vnny8.gerenciamento_de_gastos.usuario;

import com.vnny8.gerenciamento_de_gastos.confirmacaoUsuario.ConfirmacaoUsuario;
import com.vnny8.gerenciamento_de_gastos.confirmacaoUsuario.ConfirmacaoUsuarioRepository;
import com.vnny8.gerenciamento_de_gastos.exceptions.usuarioExceptions.UsuarioNaoEncontrado;
import com.vnny8.gerenciamento_de_gastos.usuario.dtos.AlterarSenhaRequest;
import com.vnny8.gerenciamento_de_gastos.usuario.dtos.CriarUsuarioComumRequest;
import com.vnny8.gerenciamento_de_gastos.usuario.dtos.EditarUsuarioRequest;
import com.vnny8.gerenciamento_de_gastos.usuario.dtos.UsuarioResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioComumRepository usuarioComumRepository;
    private final ConfirmacaoUsuarioRepository confirmacaoUsuarioRepository;
    private final EmailService emailService;
    Random random = new Random();

    public UsuarioService (UsuarioRepository usuarioRepository,
    PasswordEncoder passwordEncoder,
    UsuarioComumRepository usuarioComumRepository,
    ConfirmacaoUsuarioRepository  confirmacaoUsuarioRepository,
    EmailService emailService){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioComumRepository = usuarioComumRepository;
        this.confirmacaoUsuarioRepository = confirmacaoUsuarioRepository;
        this.emailService = emailService;
    }

    public UsuarioResponse criar(CriarUsuarioComumRequest usuario) {
        UsuarioComum usuarioComum = new UsuarioComum();
        usuarioComum.setNome(usuario.nome());
        usuarioComum.setEmail(usuario.email());
        usuarioComum.setRole("ROLE_USER");
        String senhaCriptografada = passwordEncoder.encode(usuario.senha());
        usuarioComum.setSenha(senhaCriptografada);
        usuarioComumRepository.save(usuarioComum);

        // Token para confirmar conta no e-mail
        ConfirmacaoUsuario confirmacaoUsuario = new ConfirmacaoUsuario();
        String codigo = String.format("%06d", random.nextInt(999999));
        confirmacaoUsuario.setCodigo(codigo);
        confirmacaoUsuario.setCriadoEm(LocalDateTime.now());
        confirmacaoUsuario.setUsuario(usuarioComum);

        confirmacaoUsuarioRepository.save(confirmacaoUsuario);
        enviarEmailDeConfirmacao(usuarioComum, codigo);
        return usuarioParaDTOResponse(usuarioComum);
    }

    public UsuarioResponse encontrarPorEmail(String email) {
        return usuarioParaDTOResponse(encontrarUsuarioPorEmail(email));
    }

    public void deletarPorId(Long id){
        Usuario usuario = encontrarUsuarioPorId(id);
        usuarioRepository.delete(usuario);
    }

    public Usuario encontrarUsuarioPorId(Long id){
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontrado("Usuário com ID " + id + " não encontrado."));
    }

    public List<Usuario> listarTodos(){
        return usuarioRepository.findAll();
    }

    public UsuarioResponse usuarioParaDTOResponse(Usuario usuario){
        return new UsuarioResponse(
                usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getAtivo()
        );
    }

    public Usuario encontrarUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNaoEncontrado("Não existe usuário com o email " + email));
    }

    public UsuarioComum encontrarUsuarioComum(String email){
        return usuarioComumRepository.findByEmail(email)
        .orElseThrow(() -> new UsuarioNaoEncontrado("Não existe usuário com o email " + email));
    }

    public void editar(EditarUsuarioRequest usuarioEditar) {
        Optional<UsuarioComum> usuarioComumOptional = usuarioComumRepository.findById(usuarioEditar.id());
        if (usuarioComumOptional.isPresent()) {
            UsuarioComum usuarioComum = usuarioComumOptional.get();
            usuarioComum.setNome(usuarioEditar.nome());
            usuarioComum.setEmail(usuarioEditar.email());
            usuarioComumRepository.save(usuarioComum);
        } else {
            Usuario usuario = encontrarUsuarioPorId(usuarioEditar.id());
            usuario.setNome(usuarioEditar.nome());
            usuarioRepository.save(usuario);
        }
    }

    public void confirmarConta(String codigo, String email) {
        Optional<ConfirmacaoUsuario> optionalCodigo = confirmacaoUsuarioRepository.findByCodigoAndEmail(codigo, email);

        if (optionalCodigo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Código inválido!");
        }

        ConfirmacaoUsuario codigoConfirmacao = optionalCodigo.get();

        if (codigoConfirmacao.getConfirmouEm() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código já confirmado!");
        }

        // Atualizar o token como confirmado
        codigoConfirmacao.setConfirmouEm(LocalDateTime.now());
        confirmacaoUsuarioRepository.save(codigoConfirmacao);

        // Ativar o usuário associado ao token
        Usuario usuario = codigoConfirmacao.getUsuario();
        usuario.setAtivo(true);
        usuarioRepository.save(usuario);
    }

    public void solicitarRecuperacaoSenha(String email) {
        Usuario usuario = encontrarUsuarioPorEmail(email);

        // Gerar código de 6 dígitos
        String codigo = String.format("%06d", random.nextInt(999999));

        // Criar token de recuperação
        ConfirmacaoUsuario confirmacaoUsuario = new ConfirmacaoUsuario();
        confirmacaoUsuario.setCodigo(codigo);
        confirmacaoUsuario.setUsuario(usuario);
        confirmacaoUsuario.setCriadoEm(LocalDateTime.now());
        confirmacaoUsuarioRepository.save(confirmacaoUsuario);

        // Enviar e-mail
        enviarEmailRecuperacao(usuario, codigo);
    }

    public void alterarSenha(AlterarSenhaRequest alterarSenhaRequest) {
        // Encontrar o token
        ConfirmacaoUsuario confirmacaoUsuario = confirmacaoUsuarioRepository.findByCodigoAndEmail(alterarSenhaRequest.codigo(), alterarSenhaRequest.emailUsuario())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Código inválido."));

        if (confirmacaoUsuario.getConfirmouEm() != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código já utilizado!");
        }

        if (confirmacaoUsuario.getCriadoEm().plusMinutes(15).isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.GONE, "O código expirou.");
        }

        // Atualizar senha do usuário
        UsuarioComum usuario = encontrarUsuarioComum(alterarSenhaRequest.emailUsuario());
        String senhaCriptografada = passwordEncoder.encode(alterarSenhaRequest.novaSenha());
        usuario.setSenha(senhaCriptografada);
        usuarioComumRepository.save(usuario);

        // Marcar token como usado
        confirmacaoUsuario.setConfirmouEm(LocalDateTime.now());
        confirmacaoUsuarioRepository.save(confirmacaoUsuario);
    }

    private void enviarEmailDeConfirmacao(Usuario usuario, String codigo) {
        String mensagem = "<p>Olá, " + usuario.getNome().split(" ")[0] + "!</p>" +
                          "<p>Obrigado por se registrar. Por favor, aqui está o código para ativar sua conta:</p>" +
                          "<h3>" + codigo + "</h3>";
        emailService.enviar(usuario.getEmail(), "Confirmação de Cadastro", mensagem);
    }

    private void enviarEmailRecuperacao(Usuario usuario, String codigo) {
        String mensagem = "<p>Olá, " + usuario.getNome() + "!</p>" +
                "<p>Você solicitou a recuperação da senha. Use o código abaixo para redefinir sua senha:</p>" +
                "<h3>" + codigo + "</h3>";
        emailService.enviar(usuario.getEmail(), "Recuperação de Senha", mensagem);
    }


}
