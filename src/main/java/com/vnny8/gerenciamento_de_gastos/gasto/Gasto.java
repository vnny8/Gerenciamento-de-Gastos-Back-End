package com.vnny8.gerenciamento_de_gastos.gasto;

import com.vnny8.gerenciamento_de_gastos.categoria.Categoria;
import com.vnny8.gerenciamento_de_gastos.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Gasto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    private Float valor;

    @Column(nullable = false)
    private LocalDateTime dataCadastro;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Categoria categoria;
}
