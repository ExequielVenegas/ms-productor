package com.duoc.guiatransporte.productor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/")
public class InicioController {
    @GetMapping
    public ResponseEntity<Map<String, String>> home() {
        return ResponseEntity.ok(Map.of("mensaje", "MS-Productor Online", "status", "OK"));
    }
}