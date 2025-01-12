package com.vnny8.gerenciamento_de_gastos.categoria;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vnny8.gerenciamento_de_gastos.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(length = 30)
    private String corCategoria;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnoreProperties("categorias")
    private Usuario usuario;
}
