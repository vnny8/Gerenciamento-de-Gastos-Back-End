package com.vnny8.gerenciamento_de_gastos.usuario;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping
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

}
