package com.vnny8.gerenciamento_de_gastos.salario.dtos;

import jakarta.validation.constraints.NotNull;

public record CreateSalarioRequest (

        @NotNull
        String emailUsuario,

        @NotNull
        Float valor,

        @NotNull
        String mes,

        @NotNull
        String ano

){}
