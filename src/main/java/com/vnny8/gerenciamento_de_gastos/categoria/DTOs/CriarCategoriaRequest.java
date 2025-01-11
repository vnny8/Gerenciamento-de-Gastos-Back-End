package com.vnny8.gerenciamento_de_gastos.categoria.DTOs;

import jakarta.validation.constraints.Size;

public record CriarCategoriaRequest (

        @Size(min = 1, max = 50)
        String nome,

        @Size(min = 1, max = 30)
        String cor_categoria,

        String loginUsuario
){}