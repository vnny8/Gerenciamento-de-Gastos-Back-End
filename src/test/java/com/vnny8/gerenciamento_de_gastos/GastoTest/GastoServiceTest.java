package com.vnny8.gerenciamento_de_gastos.GastoTest;

import com.vnny8.gerenciamento_de_gastos.categoria.Categoria;
import com.vnny8.gerenciamento_de_gastos.categoria.CategoriaService;
import com.vnny8.gerenciamento_de_gastos.exceptions.gastoExceptions.GastoNaoEncontrado;
import com.vnny8.gerenciamento_de_gastos.gasto.Gasto;
import com.vnny8.gerenciamento_de_gastos.gasto.GastoRepository;
import com.vnny8.gerenciamento_de_gastos.gasto.GastoService;
import com.vnny8.gerenciamento_de_gastos.gasto.dtos.AcessarGastoResponse;
import com.vnny8.gerenciamento_de_gastos.gasto.dtos.CriarGastoRequest;
import com.vnny8.gerenciamento_de_gastos.gasto.dtos.EditarGastoRequest;
import com.vnny8.gerenciamento_de_gastos.salario.Salario;
import com.vnny8.gerenciamento_de_gastos.salario.SalarioRepository;
import com.vnny8.gerenciamento_de_gastos.usuario.Usuario;
import com.vnny8.gerenciamento_de_gastos.usuario.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class GastoServiceTest {

    @Mock
    private GastoRepository gastoRepository;

    @Mock
    private CategoriaService categoriaService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private SalarioRepository salarioRepository;

    @InjectMocks
    private GastoService gastoService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarGastoComSucesso() {
        // Arrange
        CriarGastoRequest request = new CriarGastoRequest("usuario@teste.com", "Teste Gasto", 100.0F, LocalDateTime.now(), 1L);
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Categoria categoria = new Categoria();
        categoria.setId(1L);

        when(usuarioService.encontrarUsuarioPorEmail("usuario@teste.com")).thenReturn(usuario);
        when(categoriaService.acessarCategoria(1L)).thenReturn(categoria);

        // Act
        gastoService.criar(request);

        // Assert
        verify(gastoRepository, times(1)).save(any(Gasto.class));
    }

    @Test
    void deveLancarExcecaoAoAcessarGastoNaoExistente() {
        // Arrange
        when(gastoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> gastoService.acessar(1L))
                .isInstanceOf(GastoNaoEncontrado.class)
                .hasMessage("Não existe gasto com o ID 1");
    }

    @Test
    void deveAcessarGastoComSucesso() {
        // Arrange
        Gasto gasto = new Gasto();
        gasto.setId(1L);
        when(gastoRepository.findById(1L)).thenReturn(Optional.of(gasto));

        // Act
        Gasto resultado = gastoService.acessar(1L);

        // Assert
        assertThat(resultado.getId()).isEqualTo(1L);
    }

    @Test
    void deveDeletarGastoComSucesso() {
        // Arrange
        Gasto gasto = new Gasto();
        gasto.setId(1L);
        when(gastoRepository.findById(1L)).thenReturn(Optional.of(gasto));

        // Act
        gastoService.deletar(1L);

        // Assert
        verify(gastoRepository, times(1)).delete(gasto);
    }

    @Test
    void deveEditarGastoComSucesso() {
        // Arrange
        EditarGastoRequest request = new EditarGastoRequest(1L, "Gasto Editado", 200.0f, LocalDateTime.now(), 1L);
        Gasto gasto = new Gasto();
        Categoria categoria = new Categoria();
        categoria.setId(1L);

        when(gastoRepository.findById(1L)).thenReturn(Optional.of(gasto));
        when(categoriaService.acessarCategoria(1L)).thenReturn(categoria);

        // Act
        gastoService.editar(request);

        // Assert
        verify(gastoRepository, times(1)).save(gasto);
        assertThat(gasto.getNome()).isEqualTo("Gasto Editado");
        assertThat(gasto.getValor()).isEqualTo(200.0f);
    }

    @Test
    void deveListarGastosComSucesso() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Categoria categoria = new Categoria();
        categoria.setId(10L);
        categoria.setNome("Categoria Teste");
        categoria.setCorCategoria("#FF5733");

        Gasto gasto1 = new Gasto();
        gasto1.setId(1L);
        gasto1.setUsuario(usuario);
        gasto1.setCategoria(categoria); // Associação da categoria
        gasto1.setNome("Gasto Teste");
        gasto1.setValor(100.0f);
        LocalDateTime dataAgora = LocalDateTime.now();
        gasto1.setDataCadastro(dataAgora);

        List<Gasto> gastos = List.of(gasto1);

        when(usuarioService.encontrarUsuarioPorEmail("usuario@teste.com")).thenReturn(usuario);
        when(gastoRepository.findByUsuario(usuario)).thenReturn(gastos);

        // Act
        List<AcessarGastoResponse> response = gastoService.listarGastos("usuario@teste.com");

        // Assert
        assertThat(response).hasSize(1);
        assertThat(response.get(0).id()).isEqualTo(1L);
        assertThat(response.get(0).idCategoria()).isEqualTo(10L);
        assertThat(response.get(0).nome()).isEqualTo("Gasto Teste");
        assertThat(response.get(0).valor()).isEqualTo(100.0f);
        assertThat(response.get(0).dataCadastro()).isEqualTo(dataAgora);
    }

    @Test
    void deveListarGastosPorDataComSucesso() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        List<Gasto> gastos = new ArrayList<>();
        List<Salario> salarios = new ArrayList<>();
        Salario salario = new Salario();
        salario.setValor(5000.0f);
        salarios.add(salario);

        when(usuarioService.encontrarUsuarioPorEmail("usuario@teste.com")).thenReturn(usuario);
        when(salarioRepository.findUltimoSalarioDoMes(usuario, "Janeiro", "2025")).thenReturn(salarios);
        when(gastoRepository.findByUsuarioAndData(usuario, 1, 2025)).thenReturn(gastos);
        when(gastoRepository.findSomaGastosPorMesEAno(usuario, 1, 2025)).thenReturn(1000.0f);

        // Act
        var response = gastoService.listarGastosPorData("Janeiro", "2025", "usuario@teste.com");

        // Assert
        assertThat(response.salarioDoMes()).isEqualTo(5000.0f);
        assertThat(response.valorGastoNoMes()).isEqualTo(1000.0f);
    }
}

