package com.vnny8.gerenciamento_de_gastos.security;

import com.vnny8.gerenciamento_de_gastos.usuario.UsuarioComumRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImplements implements UserDetailsService {

    private final UsuarioComumRepository usuarioComumRepository;

    public UserDetailsServiceImplements(UsuarioComumRepository usuarioComumRepository) {
        this.usuarioComumRepository = usuarioComumRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioComumRepository.findByLogin(username)
                .map(UserAuthenticated::new)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

}
