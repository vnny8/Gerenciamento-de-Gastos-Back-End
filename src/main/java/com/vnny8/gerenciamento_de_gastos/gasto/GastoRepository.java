package com.vnny8.gerenciamento_de_gastos.gasto;

import com.vnny8.gerenciamento_de_gastos.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GastoRepository extends JpaRepository<Gasto, Long> {

    List<Gasto> findByUsuario(Usuario usuario);

    @Query("SELECT g FROM Gasto g WHERE g.usuario = :usuario AND EXTRACT(MONTH FROM g.dataCadastro) = :mes AND EXTRACT(YEAR FROM g.dataCadastro) = :ano")
    List<Gasto> findByUsuarioAndData(@Param("usuario") Usuario usuario, @Param("mes") int mes, @Param("ano") int ano);

}
