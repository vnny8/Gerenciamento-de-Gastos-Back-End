package com.vnny8.gerenciamento_de_gastos.gasto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vnny8.gerenciamento_de_gastos.gasto.dtos.AcessarGastoResponse;
import com.vnny8.gerenciamento_de_gastos.gasto.dtos.CriarGastoRequest;
import com.vnny8.gerenciamento_de_gastos.gasto.dtos.EditarGastoRequest;
import com.vnny8.gerenciamento_de_gastos.gasto.dtos.ListarGastosPorDataRequest;

import java.util.List;

@RestController
@RequestMapping("/gasto")
public class GastoController {

    private final GastoService gastoService;
    public GastoController(GastoService gastoService){
        this.gastoService = gastoService;
    }

    @PostMapping("/criar")
    public ResponseEntity<Void> criar(@RequestBody CriarGastoRequest gasto){
        gastoService.criar(gasto);
        return ResponseEntity.status(201).body(null);
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<Void> deletar(@RequestParam("id") Long id){
        gastoService.deletar(id);
        return ResponseEntity.status(204).body(null);
    }

    @PutMapping("/editar")
    public ResponseEntity<Void> atualizar(@RequestBody EditarGastoRequest gasto){
        gastoService.editar(gasto);
        return ResponseEntity.status(204).body(null);
    }

    @GetMapping("/acessar")
    public ResponseEntity<AcessarGastoResponse> acessar(@RequestParam("id") Long id){
        return ResponseEntity.status(200).body(gastoService.acessarEspecifico(id));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<AcessarGastoResponse>> listar(@RequestParam("login") String login){
        return ResponseEntity.status(200).body(gastoService.listarGastos(login));
    }

    @GetMapping("/listarPorData")
    public ResponseEntity<List<AcessarGastoResponse>> listarGastosPorData(@RequestBody ListarGastosPorDataRequest dto){
        return ResponseEntity.status(200).body(gastoService.listarGastosPorData(dto));
    }

}
