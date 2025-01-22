package com.vnny8.gerenciamento_de_gastos.gasto;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.vnny8.gerenciamento_de_gastos.gasto.dtos.AcessarGastoResponse;
import com.vnny8.gerenciamento_de_gastos.gasto.dtos.AcessarGastoSalarioMensalResponse;
import com.vnny8.gerenciamento_de_gastos.gasto.dtos.CriarGastoRequest;
import com.vnny8.gerenciamento_de_gastos.gasto.dtos.EditarGastoRequest;

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
        try {
            gastoService.deletar(id);
            return ResponseEntity.status(204).body(null);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(409).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
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
    public ResponseEntity<List<AcessarGastoResponse>> listar(@RequestParam("email") String email){
        return ResponseEntity.status(200).body(gastoService.listarGastos(email));
    }

    @GetMapping("/listarPorData")
    public ResponseEntity<AcessarGastoSalarioMensalResponse> listarGastosPorData(
        @RequestParam String mes,
        @RequestParam String ano,
        @RequestParam String email
    ) {
        return ResponseEntity.status(200).body(gastoService.listarGastosPorData(mes, ano, email));
    }


}
