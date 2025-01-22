package com.vnny8.gerenciamento_de_gastos.usuario.dtos;

public record UsuarioResponse(
        Long idUsuario,
        String nome,
        String email
) {}
