package com.vnny8.gerenciamento_de_gastos.usuario;

import com.vnny8.gerenciamento_de_gastos.exceptions.usuarioExceptions.UsuarioNaoEncontrado;
import com.vnny8.gerenciamento_de_gastos.usuario.dtos.CriarUsuarioComumRequest;
import com.vnny8.gerenciamento_de_gastos.usuario.dtos.EditarUsuarioRequest;
import com.vnny8.gerenciamento_de_gastos.usuario.dtos.UsuarioResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioComumRepository usuarioComumRepository;
    public UsuarioService (UsuarioRepository usuarioRepository,
    PasswordEncoder passwordEncoder,
    UsuarioComumRepository usuarioComumRepository){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioComumRepository = usuarioComumRepository;
    }

    public UsuarioResponse criar(CriarUsuarioComumRequest usuario) {
        UsuarioComum usuarioComum = new UsuarioComum();
        usuarioComum.setNome(usuario.nome());
        usuarioComum.setEmail(usuario.email());
        usuarioComum.setLogin(usuario.login());
        usuarioComum.setRole("ROLE_USER");
        String senhaCriptografada = passwordEncoder.encode(usuario.senha());
        usuarioComum.setSenha(senhaCriptografada);
        usuarioComumRepository.save(usuarioComum);
        return usuarioParaDTOResponse(usuarioComum);
    }

    public UsuarioResponse encontrarPorLogin(String login) {
        return usuarioParaDTOResponse(encontrarUsuarioPorLogin(login));
    }

    public void deletarPorId(Long id){
        Usuario usuario = encontrarUsuarioPorId(id);
        usuarioRepository.delete(usuario);
    }

    public Usuario encontrarUsuarioPorId(Long id){
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontrado("Usuário com ID " + id + " não encontrado."));
    }

    public List<Usuario> listarTodos(){
        return usuarioRepository.findAll();
    }

    public UsuarioResponse usuarioParaDTOResponse(Usuario usuario){
        return new UsuarioResponse(
                usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getLogin()
        );
    }

    public Usuario encontrarUsuarioPorLogin(String login){
        return usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new UsuarioNaoEncontrado("Não existe usuário com o Login " + login));
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
            Usuario usuario = encontrarUsuarioPorId(usuarioEditar.id());
            usuario.setNome(usuarioEditar.nome());
            usuarioRepository.save(usuario);
        }
    }



}
