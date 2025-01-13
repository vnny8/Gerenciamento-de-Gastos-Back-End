package com.vnny8.gerenciamento_de_gastos.gasto.dtos;

import jakarta.validation.constraints.NotNull;

public record ListarGastosPorDataRequest(

    @NotNull
    String mes,

    @NotNull
    String ano,

    @NotNull
    String login
){}
