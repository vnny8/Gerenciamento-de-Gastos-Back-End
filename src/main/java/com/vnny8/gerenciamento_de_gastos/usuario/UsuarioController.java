package com.vnny8.gerenciamento_de_gastos.usuario;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.vnny8.gerenciamento_de_gastos.usuario.dtos.AlterarSenhaRequest;
import com.vnny8.gerenciamento_de_gastos.usuario.dtos.CriarUsuarioComumRequest;
import com.vnny8.gerenciamento_de_gastos.usuario.dtos.EditarUsuarioRequest;
import com.vnny8.gerenciamento_de_gastos.usuario.dtos.UsuarioResponse;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @PostMapping("/criar")
    public ResponseEntity<UsuarioResponse> criar(@RequestBody CriarUsuarioComumRequest usuario) {
        return ResponseEntity.status(201).body(usuarioService.criar(usuario));
    }

    @GetMapping("/encontrarPorEmail")
    public ResponseEntity<UsuarioResponse> encontrarPorEmail(@RequestParam("email") String email){
        return ResponseEntity.status(200).body(usuarioService.encontrarPorEmail(email));
    }

    @GetMapping("/encontrarUsuarioPorId")
    public ResponseEntity<Usuario> encontrarPorID(@RequestParam("id") Long id){
        return ResponseEntity.status(200).body(usuarioService.encontrarUsuarioPorId(id));
    }

    @GetMapping("/listarTodos")
    public ResponseEntity<List<Usuario>> listarTodos(){
        return ResponseEntity.status(200).body(usuarioService.listarTodos());
    }


    @DeleteMapping("/apagar")
    public ResponseEntity<Void> deletarPorID(@RequestParam("id") Long id){
        usuarioService.deletarPorId(id);
        return ResponseEntity.status(204).body(null);
    }

    @PutMapping("/editar")
    public ResponseEntity<Void> editarPorID(@RequestBody EditarUsuarioRequest usuario) {
        usuarioService.editar(usuario);
        return ResponseEntity.status(204).body(null);
    }

    @PostMapping("/confirmarConta")
    public ResponseEntity<String> confirmarConta(@RequestParam("codigo") String codigo,
    @RequestParam("email") String email) {
        try{
            usuarioService.confirmarConta(codigo, email);
            return ResponseEntity.ok("Conta ativada com sucesso!");
        } catch (ResponseStatusException ex) {
            // Retorna o status específico do erro com a mensagem apropriada
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        } catch (Exception ex) {
            // Retorna um status genérico 500 para outros erros
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao confirmar conta.");
        }
    }

    @PostMapping("/esqueciSenha")
    public ResponseEntity<String> esqueciSenha(@RequestParam String email) {
        usuarioService.solicitarRecuperacaoSenha(email);
        return ResponseEntity.ok("E-mail de recuperação enviado com sucesso.");
    }

    @PostMapping("/alterarSenha")
    public ResponseEntity<String> alterarSenha(@RequestBody AlterarSenhaRequest alterarSenhaRequest) {
        try{
            usuarioService.alterarSenha(alterarSenhaRequest);
            return ResponseEntity.ok("Senha alterada com sucesso.");
        } catch (ResponseStatusException ex){
            // Retorna o status específico do erro com a mensagem apropriada
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        } catch (Exception ex) {
            // Retorna um status genérico 500 para outros erros
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao alterar senha.");
        }
    }

}
