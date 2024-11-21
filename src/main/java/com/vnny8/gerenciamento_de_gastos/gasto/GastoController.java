package com.vnny8.gerenciamento_de_gastos.gasto;

import com.vnny8.gerenciamento_de_gastos.gasto.DTOs.AcessarGastoResponse;
import com.vnny8.gerenciamento_de_gastos.gasto.DTOs.CriarGastoRequest;
import com.vnny8.gerenciamento_de_gastos.gasto.DTOs.EditarGastoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gasto")
public class GastoController {

    @Autowired
    private GastoService gastoService;

    @PostMapping("/criar")
    public ResponseEntity<?> criar(@RequestBody CriarGastoRequest gasto){
        gastoService.criar(gasto);
        return ResponseEntity.status(201).body(null);
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<?> deletar(@RequestParam("id") Long id){
        gastoService.deletar(id);
        return ResponseEntity.status(204).body(null);
    }

    @PutMapping("/editar")
    public ResponseEntity<?> atualizar(@RequestBody EditarGastoRequest gasto){
        gastoService.editar(gasto);
        return ResponseEntity.status(204).body(null);
    }

    @GetMapping("/acessar")
    public ResponseEntity<AcessarGastoResponse> acessar(@RequestParam("id") Long id){
        return ResponseEntity.status(200).body(gastoService.acessarEspecifico(id));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<AcessarGastoResponse>> listar(@RequestParam("id_usuario") Long id_usuario){
        return ResponseEntity.status(200).body(gastoService.listarGastos(id_usuario));
    }

}
