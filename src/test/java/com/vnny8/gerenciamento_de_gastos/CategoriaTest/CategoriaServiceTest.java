package com.vnny8.gerenciamento_de_gastos.CategoriaTest;

import com.vnny8.gerenciamento_de_gastos.categoria.Categoria;
import com.vnny8.gerenciamento_de_gastos.categoria.CategoriaRepository;
import com.vnny8.gerenciamento_de_gastos.categoria.CategoriaService;
import com.vnny8.gerenciamento_de_gastos.categoria.dtos.CategoriaResponse;
import com.vnny8.gerenciamento_de_gastos.categoria.dtos.CriarCategoriaRequest;
import com.vnny8.gerenciamento_de_gastos.categoria.dtos.EditarCategoriaRequest;
import com.vnny8.gerenciamento_de_gastos.exceptions.categoriaExceptions.CategoriaNaoEncontrada;
import com.vnny8.gerenciamento_de_gastos.usuario.Usuario;
import com.vnny8.gerenciamento_de_gastos.usuario.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private CategoriaService categoriaService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarCategoriaComSucesso() {
        // Arrange
        CriarCategoriaRequest request = new CriarCategoriaRequest("Categoria Teste", "#FF5733", "usuario@teste.com");
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("usuario@teste.com");

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Categoria Teste");
        categoria.setCorCategoria("#FF5733");
        categoria.setUsuario(usuario);

        when(usuarioService.encontrarUsuarioPorEmail("usuario@teste.com")).thenReturn(usuario);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        // Act
        CategoriaResponse response = categoriaService.criar(request);

        // Assert
        assertThat(response.categoriaId()).isEqualTo(1L);
        assertThat(response.nome()).isEqualTo("Categoria Teste");
        assertThat(response.cor_categoria()).isEqualTo("#FF5733");
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void deveRetornarErroQuandoUsuarioNaoEncontradoAoCriarCategoria() {
        // Arrange
        CriarCategoriaRequest request = new CriarCategoriaRequest("Categoria Teste", "#FF5733", "usuario@invalido.com");
        when(usuarioService.encontrarUsuarioPorEmail("usuario@invalido.com"))
                .thenThrow(new IllegalArgumentException("Usuário não encontrado"));

        // Act & Assert
        assertThatThrownBy(() -> categoriaService.criar(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Usuário não encontrado");
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    void deveAcessarCategoriaPorIdComSucesso() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Categoria Teste");
        categoria.setUsuario(usuario);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        // Act
        CategoriaResponse response = categoriaService.acessar(1L);

        // Assert
        assertThat(response.categoriaId()).isEqualTo(1L);
        assertThat(response.nome()).isEqualTo("Categoria Teste");
        verify(categoriaRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoCategoriaNaoForEncontrada() {
        // Arrange
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> categoriaService.acessar(1L))
                .isInstanceOf(CategoriaNaoEncontrada.class)
                .hasMessage("Não existe categoria com o ID 1");
    }

    @Test
    void deveDeletarCategoriaComSucesso() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("usuario@teste.com");

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setUsuario(usuario);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        // Act
        categoriaService.deletar(1L);

        // Assert
        verify(categoriaRepository, times(1)).deleteById(1L);
    }


    @Test
    void deveEditarCategoriaComSucesso() {
        // Arrange
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Antigo Nome");
        categoria.setCorCategoria("#FFFFFF");

        EditarCategoriaRequest request = new EditarCategoriaRequest(1L, "Novo Nome", "#000000");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        // Act
        categoriaService.editar(request);

        // Assert
        assertThat(categoria.getNome()).isEqualTo("Novo Nome");
        assertThat(categoria.getCorCategoria()).isEqualTo("#000000");
        verify(categoriaRepository, times(1)).save(categoria);
    }

    @Test
    void deveListarCategoriasPorUsuarioComSucesso() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("usuario@teste.com");

        Categoria categoria1 = new Categoria();
        categoria1.setId(1L);
        categoria1.setNome("Categoria 1");
        categoria1.setUsuario(usuario);

        Categoria categoria2 = new Categoria();
        categoria2.setId(2L);
        categoria2.setNome("Categoria 2");
        categoria2.setUsuario(usuario);

        when(usuarioService.encontrarUsuarioPorEmail("usuario@teste.com")).thenReturn(usuario);
        when(categoriaRepository.findCategoriasByUsuario(usuario)).thenReturn(List.of(categoria1, categoria2));

        // Act
        List<CategoriaResponse> responseList = categoriaService.listar("usuario@teste.com");

        // Assert
        assertThat(responseList).hasSize(2);
        assertThat(responseList.get(0).nome()).isEqualTo("Categoria 1");
        assertThat(responseList.get(1).nome()).isEqualTo("Categoria 2");
    }
}
