package com.duoc.guiatransporte.productor.controller;

import com.duoc.guiatransporte.productor.config.RabbitMQConfig;
import com.duoc.guiatransporte.productor.dto.DespachoDTO;
import com.duoc.guiatransporte.productor.model.Despacho;
import com.duoc.guiatransporte.productor.service.DespachoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/despachos")
@RequiredArgsConstructor
public class DespachoController {

    private final DespachoService despachoService;
    private final RabbitTemplate rabbitTemplate;

    @PostMapping
    public ResponseEntity<String> crearAsincrono(@RequestBody Despacho despacho) {
        try {
            despacho.setEstado("EN_PROCESO_ASINCRONO");
            Despacho guardado = despachoService.crearInicial(despacho);

            DespachoDTO dto = new DespachoDTO(guardado);

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,
                    RabbitMQConfig.ROUTING_NORMAL,
                    dto
            );

            log.info(">>> [PRODUCTOR] Mensaje publicado en Cola 1 para Despacho ID: {}", guardado.getId());

            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body("Despacho #" + guardado.getId() + " recibido exitosamente. Se ha enviado a la Cola 1 para la generación de guía en S3.");
        } catch (Exception e) {
            log.error(">>> [PRODUCTOR] Error al procesar solicitud: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar solicitud: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<DespachoDTO>> obtenerTodos() {
        return ResponseEntity.ok(despachoService.obtenerTodos().stream().map(DespachoDTO::new).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DespachoDTO> obtenerPorId(@PathVariable Long id) {
        try { return ResponseEntity.ok(new DespachoDTO(despachoService.obtenerPorId(id))); }
        catch (Exception e) { return ResponseEntity.status(404).build(); }
    }

    @GetMapping("/{id}/guia/descargar")
    public ResponseEntity<byte[]> descargarGuia(@PathVariable Long id) {
        try {
            byte[] pdf = despachoService.descargarGuia(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"guia_" + id + ".pdf\"")
                    .contentType(MediaType.APPLICATION_PDF).body(pdf);
        } catch (Exception e) { return ResponseEntity.status(404).build(); }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DespachoDTO> modificar(@PathVariable Long id, @RequestBody Despacho datos) {
        try { return ResponseEntity.ok(new DespachoDTO(despachoService.modificar(id, datos))); }
        catch (Exception e) { return ResponseEntity.status(500).build(); }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        try { despachoService.eliminar(id); return ResponseEntity.ok("Despacho " + id + " eliminado."); }
        catch (Exception e) { return ResponseEntity.status(500).body("Error: " + e.getMessage()); }
    }

    @GetMapping("/consultar")
    public ResponseEntity<List<String>> consultar(@RequestParam String fecha, @RequestParam String transportista) {
        try { return ResponseEntity.ok(despachoService.consultarPorFechaYTransportista(fecha, transportista)); }
        catch (Exception e) { return ResponseEntity.status(500).build(); }
    }
}