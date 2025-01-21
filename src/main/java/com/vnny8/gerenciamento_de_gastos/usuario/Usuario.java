package com.vnny8.gerenciamento_de_gastos.usuario;

import com.vnny8.gerenciamento_de_gastos.categoria.Categoria;
import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Table(name = "usuario")
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, unique = true, length = 100)
    private String login;

    @Column(nullable = false)
    private String role;

    // Não queremos serializar as categorias na resposta JSON
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Categoria> categorias;
}
