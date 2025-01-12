package com.vnny8.gerenciamento_de_gastos.gasto.dtos;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AcessarGastoResponse(

        @NotNull
        String nome,

        @NotNull
        Float valor,

        @NotNull
        LocalDateTime dataCadastro,

        @NotNull
        String categoria
){}
