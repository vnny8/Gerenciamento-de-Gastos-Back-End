package com.vnny8.gerenciamento_de_gastos.gasto.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record CriarGastoRequest(

        @NotNull
        String loginUsuario,

        @NotNull
        String nome,

        @NotNull
        Float valor,

        @NotNull
        Long idCategoria,

        @NotNull
        LocalDateTime dataCadastro
) {}
