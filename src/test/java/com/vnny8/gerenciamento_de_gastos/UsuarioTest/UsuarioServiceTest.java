package com.vnny8.gerenciamento_de_gastos.UsuarioTest;

import com.vnny8.gerenciamento_de_gastos.confirmacaoUsuario.ConfirmacaoUsuario;
import com.vnny8.gerenciamento_de_gastos.confirmacaoUsuario.ConfirmacaoUsuarioRepository;
import com.vnny8.gerenciamento_de_gastos.exceptions.usuarioExceptions.UsuarioNaoEncontrado;
import com.vnny8.gerenciamento_de_gastos.usuario.*;
import com.vnny8.gerenciamento_de_gastos.usuario.dtos.AlterarSenhaRequest;
import com.vnny8.gerenciamento_de_gastos.usuario.dtos.CriarUsuarioComumRequest;
import com.vnny8.gerenciamento_de_gastos.usuario.dtos.UsuarioResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioComumRepository usuarioComumRepository;

    @Mock
    private ConfirmacaoUsuarioRepository confirmacaoUsuarioRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
void deveCriarUsuarioComSucesso() {
    // Arrange
    CriarUsuarioComumRequest request = new CriarUsuarioComumRequest("Usuario Teste", "Senha123!", "teste@email.com");
    UsuarioComum usuarioComum = new UsuarioComum();
    usuarioComum.setNome("Usuario Teste");
    usuarioComum.setEmail("teste@email.com");
    usuarioComum.setSenha("senhaCriptografada");

    when(passwordEncoder.encode("Senha123!")).thenReturn("senhaCriptografada");
    when(usuarioComumRepository.save(any(UsuarioComum.class)))
            .thenAnswer(invocation -> {
                UsuarioComum usuario = invocation.getArgument(0);
                usuario.setId(1L); // Simula a atribuição de ID pelo banco de dados
                return usuario;
            });

    // Mock para salvar o token de confirmação
    when(confirmacaoUsuarioRepository.save(any(ConfirmacaoUsuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    UsuarioResponse response = usuarioService.criar(request);

    // Assert
    assertThat(response.idUsuario()).isEqualTo(1L); // ID deve ser 1
    assertThat(response.nome()).isEqualTo("Usuario Teste"); // Nome correto
    assertThat(response.email()).isEqualTo("teste@email.com"); // E-mail correto
    assertThat(response.ativo()).isFalse(); // Usuário deve começar como inativo

    // Verificar se o e-mail foi enviado
    verify(emailService, times(1)).enviar(eq("teste@email.com"), eq("Confirmação de Cadastro"), anyString());
}



    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontradoPorEmail() {
        // Arrange
        when(usuarioRepository.findByEmail("nao_existe@email.com")).thenReturn(Optional.empty());

        // Act & Assert
        UsuarioNaoEncontrado exception = assertThrows(UsuarioNaoEncontrado.class, () ->
                usuarioService.encontrarUsuarioPorEmail("nao_existe@email.com"));
        assertThat(exception.getMessage()).isEqualTo("Não existe usuário com o email nao_existe@email.com");
    }

    @Test
    void deveAlterarSenhaComSucesso() {
        // Arrange
        UsuarioComum usuario = new UsuarioComum();
        usuario.setId(1L);
        usuario.setEmail("teste@email.com");
        usuario.setSenha("senhaAntiga");

        ConfirmacaoUsuario confirmacaoUsuario = new ConfirmacaoUsuario();
        confirmacaoUsuario.setCodigo("123456");
        confirmacaoUsuario.setUsuario(usuario);
        confirmacaoUsuario.setCriadoEm(LocalDateTime.now());

        AlterarSenhaRequest request = new AlterarSenhaRequest("123456", "teste@email.com", "NovaSenha123!");

        when(confirmacaoUsuarioRepository.findByCodigoAndEmail("123456", "teste@email.com"))
                .thenReturn(Optional.of(confirmacaoUsuario));
        when(usuarioComumRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("NovaSenha123!")).thenReturn("senhaNovaCriptografada");

        // Act
        usuarioService.alterarSenha(request);

        // Assert
        verify(usuarioComumRepository).save(usuario);
        assertThat(usuario.getSenha()).isEqualTo("senhaNovaCriptografada");
    }

    @Test
    void deveLancarExcecaoQuandoCodigoInvalidoNaAlteracaoDeSenha() {
        // Arrange
        when(confirmacaoUsuarioRepository.findByCodigoAndEmail("codigoInvalido", "teste@email.com"))
                .thenReturn(Optional.empty());

        AlterarSenhaRequest request = new AlterarSenhaRequest("codigoInvalido", "teste@email.com", "NovaSenha123!");

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                usuarioService.alterarSenha(request));
        assertThat(exception.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.NOT_FOUND);
        assertThat(exception.getReason()).isEqualTo("Código inválido.");
    }

    @Test
    void deveConfirmarContaComSucesso() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("teste@email.com");
        usuario.setAtivo(false);

        ConfirmacaoUsuario confirmacaoUsuario = new ConfirmacaoUsuario();
        confirmacaoUsuario.setCodigo("123456");
        confirmacaoUsuario.setUsuario(usuario);
        confirmacaoUsuario.setCriadoEm(LocalDateTime.now());

        when(confirmacaoUsuarioRepository.findByCodigoAndEmail("123456", "teste@email.com"))
                .thenReturn(Optional.of(confirmacaoUsuario));

        // Act
        usuarioService.confirmarConta("123456", "teste@email.com");

        // Assert
        assertThat(usuario.getAtivo()).isTrue();
        verify(confirmacaoUsuarioRepository).save(confirmacaoUsuario);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void deveLancarExcecaoQuandoCodigoJaConfirmado() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("teste@email.com");

        ConfirmacaoUsuario confirmacaoUsuario = new ConfirmacaoUsuario();
        confirmacaoUsuario.setCodigo("123456");
        confirmacaoUsuario.setUsuario(usuario);
        confirmacaoUsuario.setConfirmouEm(LocalDateTime.now());

        when(confirmacaoUsuarioRepository.findByCodigoAndEmail("123456", "teste@email.com"))
                .thenReturn(Optional.of(confirmacaoUsuario));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                usuarioService.confirmarConta("123456", "teste@email.com"));
        assertThat(exception.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.BAD_REQUEST);
        assertThat(exception.getReason()).isEqualTo("Código já confirmado!");
    }
}
