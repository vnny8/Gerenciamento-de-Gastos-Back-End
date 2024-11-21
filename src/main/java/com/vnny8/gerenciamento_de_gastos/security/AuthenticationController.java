package com.vnny8.gerenciamento_de_gastos.security;

import com.vnny8.gerenciamento_de_gastos.usuario.Usuario;
import com.vnny8.gerenciamento_de_gastos.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    @Value("${email.admin}")
    private String email_admin;

    public AuthenticationController(AuthenticationService authenticationService, UsuarioRepository usuarioRepository) {
        this.authenticationService = authenticationService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(Authentication authentication) {
        String token = authenticationService.authenticate(authentication);
        return ResponseEntity.ok(token);
    }


    @GetMapping("/loginGoogle")
    public String loginGoogle(@AuthenticationPrincipal OidcUser principal) {
        // Verifica se o usuário já existe no banco de dados pelo email
        if (!usuarioRepository.findByEmail(principal.getAttribute("email")).isPresent()) {
            Usuario usuario = new Usuario();
            usuario.setNome(principal.getAttribute("name"));
            usuario.setEmail(principal.getAttribute("email"));

            // Verifica se o email é de um administrador
            if (usuario.getEmail().equals(email_admin)) {
                usuario.setRole("ROLE_ADMIN");
            } else {
                usuario.setRole("ROLE_USER");
            }

            // Salva o usuário no banco de dados
            usuarioRepository.save(usuario);
        }
        Usuario usuario = usuarioRepository.findByEmail(principal.getAttribute("email")).get();

        // Após salvar ou verificar, gere o token JWT
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal.getEmail(), null, List.of(new SimpleGrantedAuthority(usuario.getRole()))
        );

        // Gera o token JWT com base nas roles e nas claims
        String jwtToken = jwtService.generateToken(authentication);

        // Retorna o token JWT ao invés do token do Google
        return jwtToken;
    }


}
