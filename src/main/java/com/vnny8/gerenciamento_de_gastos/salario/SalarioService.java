package com.vnny8.gerenciamento_de_gastos.salario;

import com.vnny8.gerenciamento_de_gastos.exceptions.salarioExceptions.SalarioNaoEncontrado;
import com.vnny8.gerenciamento_de_gastos.salario.dtos.AcessarSalarioResponse;
import com.vnny8.gerenciamento_de_gastos.salario.dtos.CreateSalarioRequest;
import com.vnny8.gerenciamento_de_gastos.salario.dtos.EditarSalarioRequest;
import com.vnny8.gerenciamento_de_gastos.usuario.Usuario;
import com.vnny8.gerenciamento_de_gastos.usuario.UsuarioService;
import org.springframework.stereotype.Service;

@Service
public class SalarioService {

    private final SalarioRepository salarioRepository;
    private final UsuarioService usuarioService;

    public SalarioService(SalarioRepository salarioRepository,
    UsuarioService usuarioService){
        this.salarioRepository = salarioRepository;
        this.usuarioService = usuarioService;
    }

    public void criar(CreateSalarioRequest createSalarioRequest) {
        Usuario usuario = usuarioService.encontrarUsuarioPorLogin(createSalarioRequest.loginUsuario());
        Salario salario = new Salario();
        salario.setUsuario(usuario);
        salario.setValor(createSalarioRequest.valor());
        salario.setMes(createSalarioRequest.mes());
        salario.setAno(createSalarioRequest.ano());
        salarioRepository.save(salario);
    }

    public Salario acessar(Long id){
        return salarioRepository.findById(id)
                .orElseThrow(() -> new SalarioNaoEncontrado("Não existe salário com o ID " + id));
    }

    public AcessarSalarioResponse acessarMostrar(Long id){
        Salario salario = acessar(id);
        return new AcessarSalarioResponse(salario.getValor(),
                salario.getMes(), salario.getAno(), salario.getUsuario().getId());
    }

    public void deletar(Long id){
        Salario salario = acessar(id);
        salarioRepository.delete(salario);
    }

    public void editar(EditarSalarioRequest salarioRequest){
        Salario salario = acessar(salarioRequest.id());
        salario.setValor(salarioRequest.valor());
        salarioRepository.save(salario);
    }

}
