package com.vnny8.gerenciamento_de_gastos.usuario.dtos;

import jakarta.validation.constraints.NotNull;

public record CriarUsuarioComumRequest(

        @NotNull
        String nome,

        @NotNull
        String senha,

        @NotNull
        String email
) {}
