package com.vnny8.gerenciamento_de_gastos.gasto;

import com.vnny8.gerenciamento_de_gastos.categoria.Categoria;
import com.vnny8.gerenciamento_de_gastos.categoria.CategoriaRepository;
import com.vnny8.gerenciamento_de_gastos.exceptions.categoriaExceptions.CategoriaNaoEncontrada;
import com.vnny8.gerenciamento_de_gastos.gasto.DTOs.AcessarGastoResponse;
import com.vnny8.gerenciamento_de_gastos.gasto.DTOs.CriarGastoRequest;
import com.vnny8.gerenciamento_de_gastos.exceptions.gastoExceptions.GastoNaoEncontrado;
import com.vnny8.gerenciamento_de_gastos.exceptions.usuarioExceptions.UsuarioNaoEncontrado;
import com.vnny8.gerenciamento_de_gastos.gasto.DTOs.EditarGastoRequest;
import com.vnny8.gerenciamento_de_gastos.mapper.MapperObjects;
import com.vnny8.gerenciamento_de_gastos.usuario.Usuario;
import com.vnny8.gerenciamento_de_gastos.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class GastoService {

    @Autowired
    private GastoRepository gastoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    public void criar(@RequestBody CriarGastoRequest criarGastoRequest) {
        Gasto gasto = new Gasto();
        Usuario usuario = usuarioRepository.findById(criarGastoRequest.id_usuario())
                .orElseThrow(() -> new UsuarioNaoEncontrado("Não existe usuário com o ID " + criarGastoRequest.id_usuario()));

        gasto.setUsuario(usuario);
        gasto.setNome(criarGastoRequest.nome());
        gasto.setValor(criarGastoRequest.valor());
        Categoria categoria = categoriaRepository.findById(criarGastoRequest.id_categoria())
                        .orElseThrow(() -> new CategoriaNaoEncontrada("Não existe categoria com o ID " + criarGastoRequest.id_categoria()));
        gasto.setCategoria(categoria);
        gastoRepository.save(gasto);
    }

    public Gasto acessar(Long id){
        return gastoRepository.findById(id)
                .orElseThrow(() -> new GastoNaoEncontrado("Não existe gasto com o ID " + id));
    }

    public void deletar(Long id){
        Gasto gasto = acessar(id);
        gastoRepository.deleteById(id);
    }

    public void editar(EditarGastoRequest editarGastoRequest){
        Gasto gasto = acessar(editarGastoRequest.id());
        gasto.setNome(editarGastoRequest.nome());
        gasto.setValor(editarGastoRequest.valor());
        Categoria categoria = categoriaRepository.findById(editarGastoRequest.id_categoria())
                .orElseThrow(() -> new CategoriaNaoEncontrada("Não existe categoria com o ID " + editarGastoRequest.id_categoria()));
        gasto.setCategoria(categoria);
        gastoRepository.save(gasto);
    }

    public AcessarGastoResponse acessarEspecifico(Long id){
        Gasto gasto = acessar(id);
        return retornaDTOGasto(gasto);
    }

    public List<AcessarGastoResponse> listarGastos(Long id_usuario){
        Usuario usuario = usuarioRepository.findById(id_usuario)
                .orElseThrow(() -> new UsuarioNaoEncontrado("Não existe usuário com o ID " + id_usuario));
        List<Gasto> gastos = gastoRepository.findByUsuario(usuario);
        return retornaListaDTOs(gastos);
    }

    public AcessarGastoResponse retornaDTOGasto(Gasto gasto){
        return new AcessarGastoResponse(gasto.getNome(), gasto.getValor(), gasto.getDataCadastro(), gasto.getCategoria().getNome());
    }

    public List<AcessarGastoResponse> retornaListaDTOs(List<Gasto> gastos){
        List<AcessarGastoResponse> gastosDTO = new ArrayList<>();
        for (Gasto gasto : gastos) {
            gastosDTO.add(retornaDTOGasto(gasto));
        }
        return gastosDTO;
    }

}
