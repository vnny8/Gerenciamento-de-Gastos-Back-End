package com.vnny8.gerenciamento_de_gastos.gasto.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

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
