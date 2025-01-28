package com.vnny8.gerenciamento_de_gastos.GastoTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vnny8.gerenciamento_de_gastos.gasto.GastoController;
import com.vnny8.gerenciamento_de_gastos.gasto.GastoService;
import com.vnny8.gerenciamento_de_gastos.gasto.dtos.AcessarGastoResponse;
import com.vnny8.gerenciamento_de_gastos.gasto.dtos.AcessarGastoSalarioMensalResponse;
import com.vnny8.gerenciamento_de_gastos.gasto.dtos.CriarGastoRequest;
import com.vnny8.gerenciamento_de_gastos.gasto.dtos.EditarGastoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GastoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GastoService gastoService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        GastoController gastoController = new GastoController(gastoService);
        mockMvc = MockMvcBuilders.standaloneSetup(gastoController).build();

        // Registrar o módulo no ObjectMapper
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }


    @Test
    void deveCriarGastoComSucesso() throws Exception {
        // Arrange
        CriarGastoRequest request = new CriarGastoRequest("usuario@teste.com", "Gasto Teste", 100.0f, LocalDateTime.now(), 1L);

        // Act & Assert
        mockMvc.perform(post("/gasto/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(gastoService, times(1)).criar(any(CriarGastoRequest.class));
    }

    @Test
    void deveDeletarGastoComSucesso() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/gasto/deletar").param("id", "1"))
                .andExpect(status().isNoContent());

        verify(gastoService, times(1)).deletar(1L);
    }

    @Test
    void deveAtualizarGastoComSucesso() throws Exception {
        // Arrange
        EditarGastoRequest request = new EditarGastoRequest(1L, "Gasto Atualizado", 150.0f, LocalDateTime.now(), 2L);

        // Act & Assert
        mockMvc.perform(put("/gasto/editar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(gastoService, times(1)).editar(any(EditarGastoRequest.class));
    }

    @Test
    void deveAcessarGastoComSucesso() throws Exception {
        // Arrange
        AcessarGastoResponse response = new AcessarGastoResponse(1L, "Gasto Teste", 100.0f, LocalDateTime.now(), 1L, "Categoria Teste", "#FF5733");

        when(gastoService.acessarEspecifico(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/gasto/acessar").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Gasto Teste"))
                .andExpect(jsonPath("$.valor").value(100.0))
                .andExpect(jsonPath("$.idCategoria").value(1L))
                .andExpect(jsonPath("$.categoria").value("Categoria Teste"));

        verify(gastoService, times(1)).acessarEspecifico(1L);
    }

    @Test
    void deveListarGastosComSucesso() throws Exception {
        // Arrange
        List<AcessarGastoResponse> responseList = List.of(
                new AcessarGastoResponse(1L, "Gasto 1", 50.0f, LocalDateTime.now(), 1L, "Categoria 1", "#FF5733"),
                new AcessarGastoResponse(2L, "Gasto 2", 70.0f, LocalDateTime.now(), 2L, "Categoria 2", "#00FF00")
        );

        when(gastoService.listarGastos("usuario@teste.com")).thenReturn(responseList);

        // Act & Assert
        mockMvc.perform(get("/gasto/listar").param("email", "usuario@teste.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2)) // Verifica o tamanho do array
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Gasto 1"))
                .andExpect(jsonPath("$[1].nome").value("Gasto 2"));

        verify(gastoService, times(1)).listarGastos("usuario@teste.com");
    }


    @Test
    void deveListarGastosPorDataComSucesso() throws Exception {
        // Arrange
        AcessarGastoSalarioMensalResponse response = new AcessarGastoSalarioMensalResponse(
                2000.0f, 500.0f, List.of(new AcessarGastoResponse(1L, "Gasto Teste", 100.0f, LocalDateTime.now(), 1L, "Categoria Teste", "#FF5733"))
        );

        when(gastoService.listarGastosPorData("janeiro", "2025", "usuario@teste.com")).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/gasto/listarPorData")
                        .param("mes", "janeiro")
                        .param("ano", "2025")
                        .param("email", "usuario@teste.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.salarioDoMes").value(2000.0))
                .andExpect(jsonPath("$.valorGastoNoMes").value(500.0))
                .andExpect(jsonPath("$.gastos[0].id").value(1L))
                .andExpect(jsonPath("$.gastos[0].nome").value("Gasto Teste"));

        verify(gastoService, times(1)).listarGastosPorData("janeiro", "2025", "usuario@teste.com");
    }
}
