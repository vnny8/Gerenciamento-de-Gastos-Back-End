package com.vnny8.gerenciamento_de_gastos.usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "usuario_comum")
@EqualsAndHashCode(callSuper = true)
public class UsuarioComum extends Usuario {

    @Column(nullable = false, unique = true, length = 30)
    private String login;

    @Column(nullable = false)
    private String senha;
}
