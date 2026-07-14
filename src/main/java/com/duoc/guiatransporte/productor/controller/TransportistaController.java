package com.duoc.guiatransporte.productor.controller;

import com.duoc.guiatransporte.productor.dto.TransportistaDTO;
import com.duoc.guiatransporte.productor.model.Transportista;
import com.duoc.guiatransporte.productor.service.TransportistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transportistas")
@RequiredArgsConstructor
public class TransportistaController {
    private final TransportistaService service;

    @PostMapping
    public ResponseEntity<TransportistaDTO> crear(@RequestBody Transportista t) {
        return ResponseEntity.ok(new TransportistaDTO(service.crear(t)));
    }

    @GetMapping
    public ResponseEntity<List<TransportistaDTO>> obtenerTodos() {
        return ResponseEntity.ok(service.obtenerTodos().stream().map(TransportistaDTO::new).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransportistaDTO> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new TransportistaDTO(service.obtenerPorId(id)));
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransportistaDTO> modificar(@PathVariable Long id, @RequestBody Transportista d) {
        try {
            return ResponseEntity.ok(new TransportistaDTO(service.modifying(id, d)));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        try {
            service.eliminar(id);
            return ResponseEntity.ok("Transportista eliminado.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}