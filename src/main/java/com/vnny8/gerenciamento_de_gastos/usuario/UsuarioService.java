package com.vnny8.gerenciamento_de_gastos.usuario;

import com.vnny8.gerenciamento_de_gastos.exceptions.usuarioExceptions.UsuarioNaoEncontrado;
import com.vnny8.gerenciamento_de_gastos.usuario.DTOs.CriarUsuarioComumRequest;
import com.vnny8.gerenciamento_de_gastos.usuario.DTOs.EditarUsuarioRequest;
import com.vnny8.gerenciamento_de_gastos.usuario.DTOs.EncontrarUsuarioPorIdResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioComumRepository usuarioComumRepository;

    public void criar(CriarUsuarioComumRequest usuario) {
        UsuarioComum usuarioComum = new UsuarioComum();
        usuarioComum.setNome(usuario.nome());
        usuarioComum.setEmail(usuario.email());
        usuarioComum.setLogin(usuario.login());
        usuarioComum.setRole("ROLE_USER");
        String senhaCriptografada = passwordEncoder.encode(usuario.senha());
        usuarioComum.setSenha(senhaCriptografada);
        usuarioComumRepository.save(usuarioComum);
    }

    public EncontrarUsuarioPorIdResponse encontrarPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(usuario -> new EncontrarUsuarioPorIdResponse(usuario.getNome(), usuario.getEmail()))
                .orElseThrow(() -> new UsuarioNaoEncontrado("Usuário com ID " + id + " não encontrado."));
    }

    public void deletarPorId(Long id){
        EncontrarUsuarioPorIdResponse usuario = encontrarPorId(id);
        if (usuario != null) {
            usuarioRepository.deleteById(id);
        }
    }

    public void editar(EditarUsuarioRequest usuarioEditar) {
        Optional<UsuarioComum> usuarioComumOptional = usuarioComumRepository.findById(usuarioEditar.id());
        if (usuarioComumOptional.isPresent()) {
            UsuarioComum usuarioComum = usuarioComumOptional.get();
            usuarioComum.setNome(usuarioEditar.nome());
            usuarioComum.setEmail(usuarioEditar.email());
            usuarioComum.setLogin(usuarioEditar.login());
            usuarioComumRepository.save(usuarioComum);
        } else {
            Usuario usuario = usuarioRepository.findById(usuarioEditar.id())
                    .orElseThrow(() -> new UsuarioNaoEncontrado("Não existe usuário com o ID " + usuarioEditar.id()));
            usuario.setNome(usuarioEditar.nome());
            usuarioRepository.save(usuario);
        }
    }



}
