package com.vnny8.gerenciamento_de_gastos.gasto.dtos;

import jakarta.validation.constraints.NotNull;

public record EditarGastoRequest(

        @NotNull
        Long id,

        @NotNull
        String nome,

        @NotNull
        Float valor,

        @NotNull
        Long id_categoria
) {}
