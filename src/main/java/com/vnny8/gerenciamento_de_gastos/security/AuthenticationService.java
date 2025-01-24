package com.vnny8.gerenciamento_de_gastos.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.vnny8.gerenciamento_de_gastos.usuario.Usuario;
import com.vnny8.gerenciamento_de_gastos.usuario.UsuarioRepository;
import com.vnny8.gerenciamento_de_gastos.usuario.UsuarioService;

@Service
public class AuthenticationService{

    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    public AuthenticationService(JwtService jwtService,
    UsuarioService usuarioService) {
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    public String authenticate(Authentication authentication) {

        // Recupera o usuário pelo email (ou login)
        Usuario usuario = usuarioService.encontrarUsuarioPorEmail(authentication.getName());

        // Verifica se o usuário está ativo
        if (!usuario.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "O usuário não confirmou a conta. Encontra-se inativo.");
        }

        return jwtService.generateToken(authentication);
    }
}
