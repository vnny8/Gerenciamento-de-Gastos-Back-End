package com.vnny8.gerenciamento_de_gastos.security;

import com.vnny8.gerenciamento_de_gastos.usuario.Usuario;
import com.vnny8.gerenciamento_de_gastos.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomOidcUserService extends OidcUserService {

    @Value("${email.admin}")
    private String adminEmail;

    private final UsuarioRepository usuarioRepository;

    public CustomOidcUserService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        // Carrega o usuário usando o OidcUserService padrão
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getEmail();
        String nome = oidcUser.getFullName();

        // Verifica se o usuário já existe no banco de dados
        Usuario usuario = usuarioRepository.findByEmail(email).orElseGet(() -> {
            Usuario novoUsuario = new Usuario();
            novoUsuario.setEmail(email);
            novoUsuario.setNome(nome);
            novoUsuario.setLogin(email);

            // Define a role com base no email
            if (email.equals(adminEmail)) {
                novoUsuario.setRole("ROLE_ADMIN");
            } else {
                novoUsuario.setRole("ROLE_USER");
            }

            return usuarioRepository.save(novoUsuario);
        });

        // Adiciona as roles do usuário às authorities
        List<GrantedAuthority> mappedAuthorities = new ArrayList<>(oidcUser.getAuthorities());
        mappedAuthorities.add(new SimpleGrantedAuthority(usuario.getRole()));

        // Retorna um OidcUser com as authorities atualizadas
        return new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
    }
}

