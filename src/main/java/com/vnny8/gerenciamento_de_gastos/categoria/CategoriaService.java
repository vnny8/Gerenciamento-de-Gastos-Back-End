package com.vnny8.gerenciamento_de_gastos.categoria;

import com.vnny8.gerenciamento_de_gastos.exceptions.categoriaExceptions.CategoriaNaoEncontrada;
import com.vnny8.gerenciamento_de_gastos.exceptions.usuarioExceptions.UsuarioNaoEncontrado;
import com.vnny8.gerenciamento_de_gastos.usuario.Usuario;
import com.vnny8.gerenciamento_de_gastos.usuario.UsuarioComum;
import com.vnny8.gerenciamento_de_gastos.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void criar(Categoria categoria) {
        categoriaRepository.save(categoria);
    }

    public Categoria acessar(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNaoEncontrada("Não existe categoria com o ID " + id));
    }

    public void deletar(Long id) {
        acessar(id);
        categoriaRepository.deleteById(id);
    }

    public void editar(EditarCategoriaRequest categoriaDTO) {
        Categoria categoria = acessar(categoriaDTO.id());
        categoria.setNome(categoriaDTO.nome());
        categoria.setCor_categoria(categoriaDTO.cor_categoria());
        categoriaRepository.save(categoria);
    }

    public List<Categoria> listar(Long id_usuario){
        Usuario usuario = usuarioRepository.findById(id_usuario)
                .orElseThrow(() -> new UsuarioNaoEncontrado("Não existe usuário com o ID " + id_usuario));
        return categoriaRepository.findCategoriasByUsuario(usuario);
    }
}
