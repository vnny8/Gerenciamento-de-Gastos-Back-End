package com.vnny8.gerenciamento_de_gastos.gasto.dtos;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AcessarGastoResponse(

        @NotNull
        Long id,
        
        @NotNull
        String nome,

        @NotNull
        Float valor,

        @NotNull
        LocalDateTime dataCadastro,

        @NotNull
        Long idCategoria,

        @NotNull
        String categoria,

        @NotNull
        String corCategoria
){}
