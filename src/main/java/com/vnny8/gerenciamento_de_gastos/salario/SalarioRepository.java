package com.vnny8.gerenciamento_de_gastos.salario;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.vnny8.gerenciamento_de_gastos.usuario.Usuario;

public interface SalarioRepository extends JpaRepository<Salario, Long> {

    @Query("SELECT s FROM Salario s WHERE s.usuario = :usuario AND MONTH(s.dataCadastro) = :mes AND YEAR(s.dataCadastro) = :ano ORDER BY s.dataCadastro DESC")
    List<Salario> findUltimoSalarioDoMes(@Param("usuario") Usuario usuario, @Param("mes") int mes, @Param("ano") int ano);
}
