package com.vnny8.gerenciamento_de_gastos.salario;

import com.vnny8.gerenciamento_de_gastos.salario.DTOs.AcessarSalarioResponse;
import com.vnny8.gerenciamento_de_gastos.salario.DTOs.CreateSalarioRequest;
import com.vnny8.gerenciamento_de_gastos.salario.DTOs.EditarSalarioRequest;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/salario")
public class SalarioController {

    @Autowired
    private SalarioService salarioService;

    @PostMapping("/criar")
    public ResponseEntity<?> criar(@RequestBody CreateSalarioRequest salario) {
        salarioService.criar(salario);
        return ResponseEntity.status(201).body(null);
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<?> deletar(@RequestParam("id") Long id){
        salarioService.deletar(id);
        return ResponseEntity.status(204).body(null);
    }

    @GetMapping("/acessar")
    public ResponseEntity<AcessarSalarioResponse> acessar(@RequestParam("id") Long id){
        return ResponseEntity.status(200).body(salarioService.acessarMostrar(id));
    }

    @PutMapping("/editar")
    public ResponseEntity<?> editar(@RequestBody EditarSalarioRequest salario){
        salarioService.editar(salario);
        return ResponseEntity.status(204).body(null);
    }

}
