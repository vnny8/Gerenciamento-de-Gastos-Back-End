package com.vnny8.gerenciamento_de_gastos.gasto.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record EditarGastoRequest(

        @NotNull
        Long id,

        @NotNull
        String nome,

        @NotNull
        Float valor,

        @NotNull
        LocalDateTime data,

        @NotNull
        Long idCategoria
) {}
