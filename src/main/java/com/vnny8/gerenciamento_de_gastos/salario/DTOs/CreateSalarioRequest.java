package com.vnny8.gerenciamento_de_gastos.salario.DTOs;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record CreateSalarioRequest (

        @NotNull
        Long id_usuario,

        @NotNull
        Float valor

){}
