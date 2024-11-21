package com.vnny8.gerenciamento_de_gastos.gasto;

import com.vnny8.gerenciamento_de_gastos.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GastoRepository extends JpaRepository<Gasto, Long> {

    List<Gasto> findByUsuario(Usuario usuario);
}
