package com.vnny8.gerenciamento_de_gastos.usuario.DTOs;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record CriarUsuarioComumRequest(

        @NotNull
        String nome,

        @NotNull
        String login,

        @NotNull
        String senha,

        @NotNull
        String email
) {}
