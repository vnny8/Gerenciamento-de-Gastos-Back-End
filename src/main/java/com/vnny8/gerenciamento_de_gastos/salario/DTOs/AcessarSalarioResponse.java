package com.vnny8.gerenciamento_de_gastos.salario.DTOs;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AcessarSalarioResponse (
        Float valor,
        Boolean status,
        LocalDateTime dataCadastro,
        Long idUsuario
) {}
