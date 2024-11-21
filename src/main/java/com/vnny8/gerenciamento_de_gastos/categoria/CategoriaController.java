package com.vnny8.gerenciamento_de_gastos.categoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping("/criar")
    public ResponseEntity<?> criar(@RequestBody Categoria categoria) {
        categoriaService.criar(categoria);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<?> deletar(@RequestParam("id") Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.status(204).build();
    }

    @PutMapping("/editar")
    public ResponseEntity<?> editar(@RequestBody EditarCategoriaRequest categoria) {
        categoriaService.editar(categoria);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/acessar")
    public ResponseEntity<Categoria> acessar(@RequestParam("id") Long id) {
        return ResponseEntity.status(200).body(categoriaService.acessar(id));
    }

    @GetMapping("/listarPorUsuario")
    public ResponseEntity<List<Categoria>> listarPorUsuario(@RequestParam("id_usuario") Long id_usuario) {
        return ResponseEntity.status(200).body(categoriaService.listar(id_usuario));
    }

}
