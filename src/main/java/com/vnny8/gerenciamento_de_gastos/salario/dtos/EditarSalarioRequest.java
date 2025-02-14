package com.vnny8.gerenciamento_de_gastos.salario.dtos;

import jakarta.validation.constraints.NotNull;

public record EditarSalarioRequest(

        @NotNull
        Long id,

        @NotNull
        Float valor
) {}
