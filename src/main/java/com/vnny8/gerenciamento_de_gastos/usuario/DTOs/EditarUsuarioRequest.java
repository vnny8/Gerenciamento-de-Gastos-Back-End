package com.vnny8.gerenciamento_de_gastos.usuario.DTOs;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record EditarUsuarioRequest(

        @NotNull
        Long id,

        @NotNull
        String nome,

        @Nullable
        String login,

        @Nullable
        String email
) {}
