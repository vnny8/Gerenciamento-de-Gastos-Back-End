package com.vnny8.gerenciamento_de_gastos.categoria.DTOs;

import jakarta.validation.constraints.NotNull;

public record EditarCategoriaRequest(
        @NotNull
        Long id,

        @NotNull
        String nome,

        @NotNull
        String cor_categoria
) {}
