package com.vnny8.gerenciamento_de_gastos.gasto.DTOs;

import jakarta.validation.constraints.NotNull;

public record CriarGastoRequest(

        @NotNull
        Long id_usuario,

        @NotNull
        String nome,

        @NotNull
        Float valor,

        @NotNull
        Long id_categoria
) {}
