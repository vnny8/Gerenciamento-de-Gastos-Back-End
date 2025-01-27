package com.vnny8.gerenciamento_de_gastos.CategoriaTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vnny8.gerenciamento_de_gastos.categoria.CategoriaController;
import com.vnny8.gerenciamento_de_gastos.categoria.CategoriaService;
import com.vnny8.gerenciamento_de_gastos.categoria.dtos.CategoriaResponse;
import com.vnny8.gerenciamento_de_gastos.categoria.dtos.CriarCategoriaRequest;
import com.vnny8.gerenciamento_de_gastos.categoria.dtos.EditarCategoriaRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CategoriaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoriaService categoriaService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        CategoriaController categoriaController = new CategoriaController(categoriaService);
        mockMvc = MockMvcBuilders.standaloneSetup(categoriaController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void deveCriarCategoriaComSucesso() throws Exception {
        // Arrange
        CriarCategoriaRequest request = new CriarCategoriaRequest("Alimentação", "#FF5733", "usuario@teste.com");
        CategoriaResponse response = new CategoriaResponse(1L, "Alimentação", "#FF5733", 1L);

        when(categoriaService.criar(any(CriarCategoriaRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/categoria/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoriaId", is(1)))
                .andExpect(jsonPath("$.nome", is("Alimentação")))
                .andExpect(jsonPath("$.cor_categoria", is("#FF5733")))
                .andExpect(jsonPath("$.usuarioId", is(1)));

        verify(categoriaService, times(1)).criar(any(CriarCategoriaRequest.class));
    }

    @Test
    void deveDeletarCategoriaComSucesso() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/categoria/deletar")
                        .param("id", "1"))
                .andExpect(status().isNoContent());

        verify(categoriaService, times(1)).deletar(1L);
    }

    @Test
    void deveEditarCategoriaComSucesso() throws Exception {
        // Arrange
        EditarCategoriaRequest request = new EditarCategoriaRequest(1L, "Transporte", "#00FF00");

        // Act & Assert
        mockMvc.perform(put("/categoria/editar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(categoriaService, times(1)).editar(any(EditarCategoriaRequest.class));
    }

    @Test
    void deveAcessarCategoriaComSucesso() throws Exception {
        // Arrange
        CategoriaResponse response = new CategoriaResponse(1L, "Alimentação", "#FF5733", 1L);

        when(categoriaService.acessar(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/categoria/acessar")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoriaId", is(1)))
                .andExpect(jsonPath("$.nome", is("Alimentação")))
                .andExpect(jsonPath("$.cor_categoria", is("#FF5733")))
                .andExpect(jsonPath("$.usuarioId", is(1)));

        verify(categoriaService, times(1)).acessar(1L);
    }

    @Test
    void deveListarCategoriasPorUsuario() throws Exception {
        // Arrange
        List<CategoriaResponse> responseList = List.of(
                new CategoriaResponse(1L, "Alimentação", "#FF5733", 1L),
                new CategoriaResponse(2L, "Transporte", "#00FF00", 1L)
        );

        when(categoriaService.listar("usuario@teste.com")).thenReturn(responseList);

        // Act & Assert
        mockMvc.perform(get("/categoria/listarPorUsuario")
                        .param("email", "usuario@teste.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].categoriaId", is(1)))
                .andExpect(jsonPath("$[0].nome", is("Alimentação")))
                .andExpect(jsonPath("$[0].cor_categoria", is("#FF5733")))
                .andExpect(jsonPath("$[1].categoriaId", is(2)))
                .andExpect(jsonPath("$[1].nome", is("Transporte")))
                .andExpect(jsonPath("$[1].cor_categoria", is("#00FF00")));

        verify(categoriaService, times(1)).listar("usuario@teste.com");
    }
}
