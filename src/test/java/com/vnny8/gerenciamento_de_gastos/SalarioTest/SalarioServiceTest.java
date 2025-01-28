package com.vnny8.gerenciamento_de_gastos.SalarioTest;

import com.vnny8.gerenciamento_de_gastos.exceptions.salarioExceptions.SalarioNaoEncontrado;
import com.vnny8.gerenciamento_de_gastos.salario.Salario;
import com.vnny8.gerenciamento_de_gastos.salario.SalarioRepository;
import com.vnny8.gerenciamento_de_gastos.salario.SalarioService;
import com.vnny8.gerenciamento_de_gastos.salario.dtos.AcessarSalarioResponse;
import com.vnny8.gerenciamento_de_gastos.salario.dtos.CreateSalarioRequest;
import com.vnny8.gerenciamento_de_gastos.salario.dtos.EditarSalarioRequest;
import com.vnny8.gerenciamento_de_gastos.usuario.Usuario;
import com.vnny8.gerenciamento_de_gastos.usuario.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SalarioServiceTest {

    private SalarioService salarioService;

    @Mock
    private SalarioRepository salarioRepository;

    @Mock
    private UsuarioService usuarioService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        salarioService = new SalarioService(salarioRepository, usuarioService);
    }

    @Test
    void deveCriarSalarioComSucesso() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("teste@email.com");

        CreateSalarioRequest request = new CreateSalarioRequest("teste@email.com", 5000.0F, "Janeiro", "2025");

        when(usuarioService.encontrarUsuarioPorEmail("teste@email.com")).thenReturn(usuario);

        // Act
        salarioService.criar(request);

        // Assert
        verify(salarioRepository, times(1)).save(any(Salario.class));
    }

    @Test
    void deveAcessarSalarioComSucesso() {
        // Arrange
        Salario salario = new Salario();
        salario.setId(1L);
        salario.setValor(5000.0F);

        when(salarioRepository.findById(1L)).thenReturn(Optional.of(salario));

        // Act
        Salario resultado = salarioService.acessar(1L);

        // Assert
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getValor()).isEqualTo(5000.0F);
    }

    @Test
    void deveLancarExcecaoQuandoSalarioNaoForEncontrado() {
        // Arrange
        when(salarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> salarioService.acessar(1L))
                .isInstanceOf(SalarioNaoEncontrado.class)
                .hasMessage("Não existe salário com o ID 1");
    }

    @Test
    void deveAcessarMostrarSalarioComSucesso() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Salario salario = new Salario();
        salario.setId(1L);
        salario.setValor(5000.0F);
        salario.setMes("Janeiro");
        salario.setAno("2025");
        salario.setUsuario(usuario);

        when(salarioRepository.findById(1L)).thenReturn(Optional.of(salario));

        // Act
        AcessarSalarioResponse response = salarioService.acessarMostrar(1L);

        // Assert
        assertThat(response.valor()).isEqualTo(5000.0F);
        assertThat(response.mes()).isEqualTo("Janeiro");
        assertThat(response.ano()).isEqualTo("2025");
        assertThat(response.idUsuario()).isEqualTo(1L);
    }

    @Test
    void deveDeletarSalarioComSucesso() {
        // Arrange
        Salario salario = new Salario();
        salario.setId(1L);

        when(salarioRepository.findById(1L)).thenReturn(Optional.of(salario));

        // Act
        salarioService.deletar(1L);

        // Assert
        verify(salarioRepository, times(1)).delete(salario);
    }

    @Test
    void deveEditarSalarioComSucesso() {
        // Arrange
        Salario salario = new Salario();
        salario.setId(1L);
        salario.setValor(4000.0F);

        EditarSalarioRequest request = new EditarSalarioRequest(1L, 5000.0F);

        when(salarioRepository.findById(1L)).thenReturn(Optional.of(salario));

        // Act
        salarioService.editar(request);

        // Assert
        verify(salarioRepository, times(1)).save(salario);
        assertThat(salario.getValor()).isEqualTo(5000.0F);
    }

    @Test
    void deveLancarExcecaoAoEditarSalarioNaoEncontrado() {
        // Arrange
        EditarSalarioRequest request = new EditarSalarioRequest(1L, 5000.0F);

        when(salarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> salarioService.editar(request))
                .isInstanceOf(SalarioNaoEncontrado.class)
                .hasMessage("Não existe salário com o ID 1");
    }
}
