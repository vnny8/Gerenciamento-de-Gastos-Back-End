package com.vnny8.gerenciamento_de_gastos.categoria.DTOs;

public record CategoriaResponse(

        Long categoriaId,
        String nome,
        String cor_categoria,
        Long usuarioId
) {}
