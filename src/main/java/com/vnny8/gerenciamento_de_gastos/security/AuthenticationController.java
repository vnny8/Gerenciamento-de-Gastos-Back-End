package com.vnny8.gerenciamento_de_gastos.security;

import com.vnny8.gerenciamento_de_gastos.usuario.Usuario;
import com.vnny8.gerenciamento_de_gastos.usuario.UsuarioRepository;
import com.vnny8.gerenciamento_de_gastos.usuario.UsuarioService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public AuthenticationController(AuthenticationService authenticationService, 
    UsuarioRepository usuarioRepository, 
    JwtService jwtService,
    UsuarioService usuarioService) {
        this.authenticationService = authenticationService;
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @Value("${email.admin}")
    private String emailAdmin;

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(Authentication authentication) {
        try {
            String token = authenticationService.authenticate(authentication);
            return ResponseEntity.ok(token);
        } catch (ResponseStatusException ex) {
            // Retorna o status específico do erro com a mensagem apropriada
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        } catch (Exception ex) {
            // Retorna um status genérico 500 para outros erros
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao autenticar");
        }
    }


    @GetMapping("/loginGoogle")
    public String loginGoogle(@AuthenticationPrincipal OidcUser principal) {
        // Verifica se o usuário já existe no banco de dados pelo email
        String emailCriar = principal.getAttribute("email");
        if (!usuarioRepository.findByEmail(emailCriar).isPresent()) {
            Usuario usuario = new Usuario();
            usuario.setNome(principal.getAttribute("name"));
            usuario.setEmail(emailCriar);

            // Verifica se o email é de um administrador
            if (usuario.getEmail().equals(emailAdmin)) {
                usuario.setRole("ROLE_ADMIN");
            } else {
                usuario.setRole("ROLE_USER");
            }

            // Salva o usuário no banco de dados
            usuarioRepository.save(usuario);
        }
        Usuario usuario = usuarioService.encontrarUsuarioPorEmail(emailCriar);

        // Após salvar ou verificar, gere o token JWT
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal.getEmail(), null, List.of(new SimpleGrantedAuthority(usuario.getRole()))
        );

        // Gera o token JWT com base nas roles e nas claims. E retorna o token JWT ao invés do token do Google
        return jwtService.generateToken(authentication);
    }

    @GetMapping("/loginGoogleApi")
    public void loginGoogle(@AuthenticationPrincipal OidcUser principal, HttpServletResponse response) throws IOException {
        String email = principal.getAttribute("email");

        Usuario usuario = usuarioService.encontrarUsuarioPorEmail(email);

        // Gera o token JWT
        String token = jwtService.generateToken(
            new UsernamePasswordAuthenticationToken(email, null, List.of(new SimpleGrantedAuthority(usuario.getRole())))
        );

        // Redireciona para o front-end com o token na URL
        response.sendRedirect("http://localhost:3000/home?token=" + token + "&email=" + email);
    }
}
