package com.vnny8.gerenciamento_de_gastos.categoria;

import com.vnny8.gerenciamento_de_gastos.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    @Query("SELECT DISTINCT c FROM Categoria c JOIN Gasto g ON g.categoria = c WHERE g.usuario = :usuario")
    List<Categoria> findCategoriasByUsuario(@Param("usuario") Usuario usuario);
}
