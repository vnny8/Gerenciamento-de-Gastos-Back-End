package com.vnny8.gerenciamento_de_gastos.usuario.dtos;

import jakarta.validation.constraints.NotNull;

public record AlterarSenhaRequest (

    @NotNull
    String codigo,

    @NotNull
    String emailUsuario,

    @NotNull
    String novaSenha
){}
