package com.vnny8.gerenciamento_de_gastos.categoria;

import com.vnny8.gerenciamento_de_gastos.categoria.DTOs.CategoriaResponse;
import com.vnny8.gerenciamento_de_gastos.categoria.DTOs.CriarCategoriaRequest;
import com.vnny8.gerenciamento_de_gastos.categoria.DTOs.EditarCategoriaRequest;
import com.vnny8.gerenciamento_de_gastos.exceptions.categoriaExceptions.CategoriaNaoEncontrada;
import com.vnny8.gerenciamento_de_gastos.usuario.Usuario;
import com.vnny8.gerenciamento_de_gastos.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioService usuarioService;

    public CategoriaResponse criar(CriarCategoriaRequest dto) {
        Categoria categoria = new Categoria();
        categoria.setNome(dto.nome());
        categoria.setCor_categoria(dto.cor_categoria());
        Usuario usuarioComum = usuarioService.encontrarUsuarioPorLogin(dto.loginUsuario());
        categoria.setUsuario(usuarioComum);
        return transformaClasseParaDTOResponse(categoriaRepository.save(categoria));
    }

    public CategoriaResponse acessar(Long id) {
        return transformaClasseParaDTOResponse(acessarCategoria(id));
    }

    public void deletar(Long id) {
        acessar(id);
        categoriaRepository.deleteById(id);
    }

    public void editar(EditarCategoriaRequest categoriaDTO) {
        Categoria categoria = acessarCategoria(categoriaDTO.id());
        categoria.setNome(categoriaDTO.nome());
        categoria.setCor_categoria(categoriaDTO.cor_categoria());
        categoriaRepository.save(categoria);
    }

    public List<CategoriaResponse> listar(String login){
        Usuario usuario = usuarioService.encontrarUsuarioPorLogin(login);
        return categoriaRepository.findCategoriasByUsuario(usuario).stream()
                .map(this::transformaClasseParaDTOResponse)
                .toList();
    }

    public Categoria acessarCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNaoEncontrada("Não existe categoria com o ID " + id));
    }

    public CategoriaResponse transformaClasseParaDTOResponse(Categoria categoria){
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNome(),
                categoria.getCor_categoria(),
                categoria.getUsuario().getId()
        );
    }
}
