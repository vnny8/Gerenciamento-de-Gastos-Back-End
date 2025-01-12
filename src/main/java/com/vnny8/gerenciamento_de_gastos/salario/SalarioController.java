package com.vnny8.gerenciamento_de_gastos.salario;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vnny8.gerenciamento_de_gastos.salario.dtos.AcessarSalarioResponse;
import com.vnny8.gerenciamento_de_gastos.salario.dtos.CreateSalarioRequest;
import com.vnny8.gerenciamento_de_gastos.salario.dtos.EditarSalarioRequest;

@RestController
@RequestMapping("/salario")
public class SalarioController {

    private final SalarioService salarioService;
    public SalarioController(SalarioService salarioService){
        this.salarioService = salarioService;
    }

    @PostMapping("/criar")
    public ResponseEntity<Void> criar(@RequestBody CreateSalarioRequest salario) {
        salarioService.criar(salario);
        return ResponseEntity.status(201).body(null);
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<Void> deletar(@RequestParam("id") Long id){
        salarioService.deletar(id);
        return ResponseEntity.status(204).body(null);
    }

    @GetMapping("/acessar")
    public ResponseEntity<AcessarSalarioResponse> acessar(@RequestParam("id") Long id){
        return ResponseEntity.status(200).body(salarioService.acessarMostrar(id));
    }

    @PutMapping("/editar")
    public ResponseEntity<Void> editar(@RequestBody EditarSalarioRequest salario){
        salarioService.editar(salario);
        return ResponseEntity.status(204).body(null);
    }

}
