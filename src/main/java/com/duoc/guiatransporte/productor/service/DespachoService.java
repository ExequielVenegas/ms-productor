package com.duoc.guiatransporte.productor.service;

import com.duoc.guiatransporte.productor.model.*;
import com.duoc.guiatransporte.productor.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DespachoService {
    private final DespachoRepository despachoRepository;
    private final TransportistaRepository transportistaRepository;
    private final GuiaDespachoRepository guiaRepository;
    private final S3Service s3Service;

    public Despacho crearInicial(Despacho d) {
        Transportista t = transportistaRepository.findById(d.getTransportista().getId())
                .orElseThrow(() -> new RuntimeException("Transportista no encontrado"));
        d.setTransportista(t);
        d.setEstado("EN_PROCESO_ASINCRONO");
        return despachoRepository.save(d);
    }

    public byte[] descargarGuia(Long despachoId) {
        GuiaDespacho guia = guiaRepository.findByDespachoOriginalId(despachoId)
                .orElseThrow(() -> new RuntimeException("Guía no encontrada en BD para despacho: " + despachoId));
        return s3Service.descargarArchivo(guia.getClaveS3());
    }

    public List<Despacho> obtenerTodos() {
        return despachoRepository.findAll();
    }

    public Despacho obtenerPorId(Long id) {
        return despachoRepository.findById(id).orElseThrow();
    }

    public Despacho modificar(Long id, Despacho datos) {
        Despacho e = obtenerPorId(id);
        e.setFechaEmision(datos.getFechaEmision());
        e.setEstado(datos.getEstado());
        e.setDescripcionCarga(datos.getDescripcionCarga());
        e.setCantidad(datos.getCantidad());
        e.setPeso(datos.getPeso());
        e.setDireccionOrigen(datos.getDireccionOrigen());
        e.setDestinatario(datos.getDestinatario());
        e.setDireccionDestino(datos.getDireccionDestino());
        return despachoRepository.save(e);
    }

    public void eliminar(Long id) {
        guiaRepository.findByDespachoOriginalId(id)
                .ifPresent(g -> s3Service.eliminarArchivo(g.getClaveS3()));
        despachoRepository.deleteById(id);
    }

    public List<String> consultarPorFechaYTransportista(String f, String t) {
        return s3Service.listarPorFechaYTransportista(f, t);
    }
}