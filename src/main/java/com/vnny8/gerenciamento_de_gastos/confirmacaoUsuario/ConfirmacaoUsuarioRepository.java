package com.vnny8.gerenciamento_de_gastos.confirmacaoUsuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConfirmacaoUsuarioRepository extends JpaRepository<ConfirmacaoUsuario, Long>{
    @Query("SELECT c FROM ConfirmacaoUsuario c WHERE c.codigo = :codigo AND c.usuario.email = :email AND c.confirmouEm IS NULL ORDER BY c.criadoEm DESC")
    Optional<ConfirmacaoUsuario> findByCodigoAndEmail(@Param("codigo") String codigo, @Param("email") String email);
}
