package com.vnny8.gerenciamento_de_gastos.usuario;

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
    public ResponseEntity<UsuarioResponse> encontrarPorID(@RequestParam("login") String login){
        return ResponseEntity.status(200).body(usuarioService.encontrarPorLogin(login));
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
