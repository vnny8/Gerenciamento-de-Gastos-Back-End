package com.vnny8.gerenciamento_de_gastos.salario;

import com.vnny8.gerenciamento_de_gastos.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Salario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Float valor;

    private String mes;

    private String ano;

    @ManyToOne
    private Usuario usuario;

}
