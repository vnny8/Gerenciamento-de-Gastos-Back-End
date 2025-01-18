package com.vnny8.gerenciamento_de_gastos.salario.dtos;

public record AcessarSalarioResponse (
        Float valor,
        String mes,
        String ano,
        Long idUsuario
) {}
