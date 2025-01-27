package com.vnny8.gerenciamento_de_gastos.UsuarioTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vnny8.gerenciamento_de_gastos.usuario.UsuarioController;
import com.vnny8.gerenciamento_de_gastos.usuario.UsuarioService;
import com.vnny8.gerenciamento_de_gastos.usuario.Usuario;
import com.vnny8.gerenciamento_de_gastos.usuario.dtos.AlterarSenhaRequest;
import com.vnny8.gerenciamento_de_gastos.usuario.dtos.CriarUsuarioComumRequest;
import com.vnny8.gerenciamento_de_gastos.usuario.dtos.EditarUsuarioRequest;
import com.vnny8.gerenciamento_de_gastos.usuario.dtos.UsuarioResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UsuarioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UsuarioService usuarioService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        UsuarioController usuarioController = new UsuarioController(usuarioService);
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void deveCriarUsuarioComSucesso() throws Exception {
        // Arrange
        CriarUsuarioComumRequest request = new CriarUsuarioComumRequest("Usuario Teste", "Senha123!", "teste@email.com");
        UsuarioResponse response = new UsuarioResponse(1L, "Usuario Teste", "teste@email.com", false);

        when(usuarioService.criar(any(CriarUsuarioComumRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/usuario/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUsuario").value(1L))
                .andExpect(jsonPath("$.nome").value("Usuario Teste"))
                .andExpect(jsonPath("$.email").value("teste@email.com"))
                .andExpect(jsonPath("$.ativo").value(false));

        verify(usuarioService, times(1)).criar(any(CriarUsuarioComumRequest.class));
    }

    @Test
    void deveEncontrarUsuarioPorEmail() throws Exception {
        // Arrange
        UsuarioResponse response = new UsuarioResponse(1L, "Usuario Teste", "teste@email.com", true);

        when(usuarioService.encontrarPorEmail("teste@email.com")).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/usuario/encontrarPorEmail").param("email", "teste@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1L))
                .andExpect(jsonPath("$.nome").value("Usuario Teste"))
                .andExpect(jsonPath("$.email").value("teste@email.com"))
                .andExpect(jsonPath("$.ativo").value(true));

        verify(usuarioService, times(1)).encontrarPorEmail("teste@email.com");
    }

    @Test
    void deveListarTodosUsuarios() throws Exception {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Usuario 1");
        usuario1.setEmail("usuario1@email.com");

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNome("Usuario 2");
        usuario2.setEmail("usuario2@email.com");

        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);

        when(usuarioService.listarTodos()).thenReturn(usuarios);

        // Act & Assert
        mockMvc.perform(get("/usuario/listarTodos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Usuario 1"))
                .andExpect(jsonPath("$[0].email").value("usuario1@email.com"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].nome").value("Usuario 2"))
                .andExpect(jsonPath("$[1].email").value("usuario2@email.com"));

        verify(usuarioService, times(1)).listarTodos();
    }

    @Test
    void deveDeletarUsuarioComSucesso() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/usuario/apagar").param("id", "1"))
                .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).deletarPorId(1L);
    }

    @Test
    void deveEditarUsuarioComSucesso() throws Exception {
        // Arrange
        EditarUsuarioRequest request = new EditarUsuarioRequest(1L, "Usuario Atualizado", "atualizado@email.com");

        // Act & Assert
        mockMvc.perform(put("/usuario/editar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).editar(any(EditarUsuarioRequest.class));
    }

    @Test
    void deveConfirmarContaComSucesso() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/usuario/confirmarConta")
                        .param("codigo", "123456")
                        .param("email", "teste@email.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("Conta ativada com sucesso!"));

        verify(usuarioService, times(1)).confirmarConta("123456", "teste@email.com");
    }

    @Test
    void deveSolicitarRecuperacaoSenha() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/usuario/esqueciSenha").param("email", "teste@email.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("E-mail de recuperação enviado com sucesso."));

        verify(usuarioService, times(1)).solicitarRecuperacaoSenha("teste@email.com");
    }

    @Test
    void deveAlterarSenhaComSucesso() throws Exception {
        // Arrange
        AlterarSenhaRequest request = new AlterarSenhaRequest("123456", "teste@email.com", "NovaSenha123!");

        // Act & Assert
        mockMvc.perform(post("/usuario/alterarSenha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Senha alterada com sucesso."));

        verify(usuarioService, times(1)).alterarSenha(any(AlterarSenhaRequest.class));
    }
}
