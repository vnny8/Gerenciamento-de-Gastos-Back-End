package com.vnny8.gerenciamento_de_gastos.categoria;

import com.vnny8.gerenciamento_de_gastos.categoria.DTOs.CategoriaResponse;
import com.vnny8.gerenciamento_de_gastos.categoria.DTOs.CriarCategoriaRequest;
import com.vnny8.gerenciamento_de_gastos.categoria.DTOs.EditarCategoriaRequest;
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
    public ResponseEntity<CategoriaResponse> criar(@RequestBody CriarCategoriaRequest categoria) {
        return ResponseEntity.status(201).body(categoriaService.criar(categoria));
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
    public ResponseEntity<CategoriaResponse> acessar(@RequestParam("id") Long id) {
        return ResponseEntity.status(200).body(categoriaService.acessar(id));
    }

    @GetMapping("/listarPorUsuario")
    public ResponseEntity<List<CategoriaResponse>> listarPorUsuario(@RequestParam("login") String login) {
        return ResponseEntity.status(200).body(categoriaService.listar(login));
    }

}
