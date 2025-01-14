package com.vnny8.gerenciamento_de_gastos.gasto.dtos;

import java.util.List;

public record AcessarGastoSalarioMensalResponse(

    Float salarioDoMes,
    Float valorGastoNoMes,
    List<AcessarGastoResponse> gastos
){}
