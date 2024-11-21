package com.vnny8.gerenciamento_de_gastos.usuario;

import com.vnny8.gerenciamento_de_gastos.usuario.DTOs.CriarUsuarioComumRequest;
import com.vnny8.gerenciamento_de_gastos.usuario.DTOs.EditarUsuarioRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/criar")
    public ResponseEntity<?> criar(@RequestBody CriarUsuarioComumRequest usuario) {
        usuarioService.criar(usuario);
        return ResponseEntity.status(201).body(null);
    }

    @GetMapping
    public ResponseEntity<?> encontrarPorID(@RequestParam("id") Long id){
        return ResponseEntity.status(200).body(usuarioService.encontrarPorId(id));
    }

    @DeleteMapping("/apagar")
    public ResponseEntity<?> deletarPorID(@RequestParam("id") Long id){
        usuarioService.deletarPorId(id);
        return ResponseEntity.status(204).body(null);
    }

    @PutMapping("/editar")
    public ResponseEntity<?> editarPorID(@RequestBody EditarUsuarioRequest usuario) {
        usuarioService.editar(usuario);
        return ResponseEntity.status(204).body(null);
    }

}
