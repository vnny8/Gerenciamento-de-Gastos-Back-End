package com.vnny8.gerenciamento_de_gastos.salario;

import com.vnny8.gerenciamento_de_gastos.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Data
public class Salario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float valor;

    private Boolean status;

    @CreationTimestamp
    private LocalDateTime dataCadastro;

    @ManyToOne
    private Usuario usuario;

}
