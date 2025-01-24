package com.vnny8.gerenciamento_de_gastos.confirmacaoUsuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmacaoUsuarioRepository extends JpaRepository<ConfirmacaoUsuario, Long>{
    Optional<ConfirmacaoUsuario> findByCodigo(String token);
}
