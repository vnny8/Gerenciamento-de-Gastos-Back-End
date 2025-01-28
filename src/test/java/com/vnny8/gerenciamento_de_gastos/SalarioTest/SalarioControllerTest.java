package com.vnny8.gerenciamento_de_gastos.SalarioTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vnny8.gerenciamento_de_gastos.salario.SalarioController;
import com.vnny8.gerenciamento_de_gastos.salario.SalarioService;
import com.vnny8.gerenciamento_de_gastos.salario.dtos.AcessarSalarioResponse;
import com.vnny8.gerenciamento_de_gastos.salario.dtos.CreateSalarioRequest;
import com.vnny8.gerenciamento_de_gastos.salario.dtos.EditarSalarioRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SalarioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SalarioService salarioService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        SalarioController salarioController = new SalarioController(salarioService);
        mockMvc = MockMvcBuilders.standaloneSetup(salarioController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void deveCriarSalarioComSucesso() throws Exception {
        // Arrange
        CreateSalarioRequest request = new CreateSalarioRequest("usuario@teste.com", 5000.0F, "Janeiro", "2025");

        // Act & Assert
        mockMvc.perform(post("/salario/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(salarioService, times(1)).criar(any(CreateSalarioRequest.class));
    }

    @Test
    void deveDeletarSalarioComSucesso() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/salario/deletar")
                        .param("id", "1"))
                .andExpect(status().isNoContent());

        verify(salarioService, times(1)).deletar(1L);
    }

    @Test
    void deveAcessarSalarioComSucesso() throws Exception {
        // Arrange
        AcessarSalarioResponse response = new AcessarSalarioResponse(5000.0F, "Janeiro", "2025", 1L);

        when(salarioService.acessarMostrar(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/salario/acessar")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor").value(5000.0F))
                .andExpect(jsonPath("$.mes").value("Janeiro"))
                .andExpect(jsonPath("$.ano").value("2025"))
                .andExpect(jsonPath("$.idUsuario").value(1L));

        verify(salarioService, times(1)).acessarMostrar(1L);
    }

    @Test
    void deveEditarSalarioComSucesso() throws Exception {
        // Arrange
        EditarSalarioRequest request = new EditarSalarioRequest(1L, 6000.0F);

        // Act & Assert
        mockMvc.perform(put("/salario/editar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(salarioService, times(1)).editar(any(EditarSalarioRequest.class));
    }
}
