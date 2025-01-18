package com.vnny8.gerenciamento_de_gastos.gasto;

import com.vnny8.gerenciamento_de_gastos.categoria.Categoria;
import com.vnny8.gerenciamento_de_gastos.categoria.CategoriaService;
import com.vnny8.gerenciamento_de_gastos.exceptions.gastoExceptions.GastoNaoEncontrado;
import com.vnny8.gerenciamento_de_gastos.gasto.dtos.AcessarGastoResponse;
import com.vnny8.gerenciamento_de_gastos.gasto.dtos.AcessarGastoSalarioMensalResponse;
import com.vnny8.gerenciamento_de_gastos.gasto.dtos.CriarGastoRequest;
import com.vnny8.gerenciamento_de_gastos.gasto.dtos.EditarGastoRequest;
import com.vnny8.gerenciamento_de_gastos.salario.Salario;
import com.vnny8.gerenciamento_de_gastos.salario.SalarioRepository;
import com.vnny8.gerenciamento_de_gastos.usuario.Usuario;
import com.vnny8.gerenciamento_de_gastos.usuario.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class GastoService {

    private final GastoRepository gastoRepository;
    private final CategoriaService categoriaService;
    private final UsuarioService usuarioService;
    private final SalarioRepository salarioRepository;

    // Injeção por construtor
    public GastoService(
        GastoRepository gastoRepository,
        CategoriaService categoriaService,
        UsuarioService usuarioService,
        SalarioRepository salarioRepository
    ) {
        this.gastoRepository = gastoRepository;
        this.categoriaService = categoriaService;
        this.usuarioService = usuarioService;
        this.salarioRepository = salarioRepository;
    }

    public void criar(@RequestBody CriarGastoRequest criarGastoRequest) {
        Gasto gasto = new Gasto();
        Usuario usuario = usuarioService.encontrarUsuarioPorLogin(criarGastoRequest.loginUsuario());

        gasto.setUsuario(usuario);
        gasto.setNome(criarGastoRequest.nome());
        gasto.setValor(criarGastoRequest.valor());
        gasto.setDataCadastro(criarGastoRequest.dataCadastro());
        Categoria categoria = categoriaService.acessarCategoria(criarGastoRequest.idCategoria());
        gasto.setCategoria(categoria);
        gastoRepository.save(gasto);
    }

    public Gasto acessar(Long id){
        return gastoRepository.findById(id)
                .orElseThrow(() -> new GastoNaoEncontrado("Não existe gasto com o ID " + id));
    }

    public void deletar(Long id){
        Gasto gasto = acessar(id);
        gastoRepository.delete(gasto);
    }

    public void editar(EditarGastoRequest editarGastoRequest){
        Gasto gasto = acessar(editarGastoRequest.id());
        gasto.setNome(editarGastoRequest.nome());
        gasto.setValor(editarGastoRequest.valor());
        Categoria categoria = categoriaService.acessarCategoria(editarGastoRequest.id_categoria());
        gasto.setCategoria(categoria);
        gastoRepository.save(gasto);
    }

    public AcessarGastoResponse acessarEspecifico(Long id){
        Gasto gasto = acessar(id);
        return retornaDTOGasto(gasto);
    }

    public List<AcessarGastoResponse> listarGastos(String login){
        Usuario usuario = usuarioService.encontrarUsuarioPorLogin(login);
        List<Gasto> gastos = gastoRepository.findByUsuario(usuario);
        return retornaListaDTOs(gastos);
    }

    public AcessarGastoSalarioMensalResponse listarGastosPorData(String mes, String ano, String login) {
        Usuario usuario = usuarioService.encontrarUsuarioPorLogin(login);
    
        // Converte mês para número
        int mesInteiro = converterMesParaNumero(mes);
        int anoInteiro = Integer.parseInt(ano);

        // Consulta o último salário registrado no mês
        List<Salario> salarios = salarioRepository.findUltimoSalarioDoMes(usuario, mes, ano);
        Float ultimoSalarioDoMes = salarios.isEmpty() ? 0.0f : salarios.get(0).getValor();
        
        // Consulta os gastos filtrados pelo mês e ano
        List<Gasto> gastos = gastoRepository.findByUsuarioAndData(usuario, mesInteiro, anoInteiro);
        Float valorGastoNoMes = gastoRepository.findSomaGastosPorMesEAno(usuario, mesInteiro, anoInteiro);
        // Retorna os DTOs
        return new AcessarGastoSalarioMensalResponse(ultimoSalarioDoMes, valorGastoNoMes, retornaListaDTOs(gastos));
    }

    public AcessarGastoResponse retornaDTOGasto(Gasto gasto){
        return new AcessarGastoResponse(gasto.getId(), gasto.getNome(), gasto.getValor(), gasto.getDataCadastro(), gasto.getCategoria().getNome(), gasto.getCategoria().getCorCategoria());
    }

    public List<AcessarGastoResponse> retornaListaDTOs(List<Gasto> gastos){
        List<AcessarGastoResponse> gastosDTO = new ArrayList<>();
        for (Gasto gasto : gastos) {
            gastosDTO.add(retornaDTOGasto(gasto));
        }
        return gastosDTO;
    }

    private int converterMesParaNumero(String mes) {
        return switch (mes.toLowerCase()) {
            case "janeiro" -> 1;
            case "fevereiro" -> 2;
            case "março" -> 3;
            case "abril" -> 4;
            case "maio" -> 5;
            case "junho" -> 6;
            case "julho" -> 7;
            case "agosto" -> 8;
            case "setembro" -> 9;
            case "outubro" -> 10;
            case "novembro" -> 11;
            case "dezembro" -> 12;
            default -> throw new IllegalArgumentException("Mês inválido: " + mes);
        };
    }
    
}
