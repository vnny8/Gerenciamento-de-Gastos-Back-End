package com.vnny8.gerenciamento_de_gastos.categoria;

import com.vnny8.gerenciamento_de_gastos.categoria.dtos.CategoriaResponse;
import com.vnny8.gerenciamento_de_gastos.categoria.dtos.CriarCategoriaRequest;
import com.vnny8.gerenciamento_de_gastos.categoria.dtos.EditarCategoriaRequest;
import com.vnny8.gerenciamento_de_gastos.exceptions.categoriaExceptions.CategoriaNaoEncontrada;
import com.vnny8.gerenciamento_de_gastos.usuario.Usuario;
import com.vnny8.gerenciamento_de_gastos.usuario.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final UsuarioService usuarioService;

    public CategoriaService(CategoriaRepository categoriaRepository,
    UsuarioService usuarioService){
        this.categoriaRepository = categoriaRepository;
        this.usuarioService = usuarioService;
    }

    public CategoriaResponse criar(CriarCategoriaRequest dto) {
        Categoria categoria = new Categoria();
        categoria.setNome(dto.nome());
        categoria.setCorCategoria(dto.cor_categoria());
        Usuario usuario = usuarioService.encontrarUsuarioPorEmail(dto.emailUsuario());
        categoria.setUsuario(usuario);
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
        categoria.setCorCategoria(categoriaDTO.cor_categoria());
        categoriaRepository.save(categoria);
    }

    public List<CategoriaResponse> listar(String email){
        Usuario usuario = usuarioService.encontrarUsuarioPorEmail(email);
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
                categoria.getCorCategoria(),
                categoria.getUsuario().getId()
        );
    }
}
