package com.vnny8.gerenciamento_de_gastos.usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioComumRepository extends JpaRepository<UsuarioComum, Long> {

    Optional<UsuarioComum> findByLogin(String login);
}
