package com.duoc.guiatransporte.productor.service;

import com.duoc.guiatransporte.productor.model.Transportista;
import com.duoc.guiatransporte.productor.repository.TransportistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransportistaService {
    private final TransportistaRepository repository;

    public Transportista crear(Transportista t) {
        return repository.save(t);
    }

    public List<Transportista> obtenerTodos() {
        return repository.findAll();
    }

    public Transportista obtenerPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Transportista no encontrado"));
    }

    public Transportista modifying(Long id, Transportista d) {
        Transportista e = obtenerPorId(id);
        e.setNombre(d.getNombre());
        e.setRut(d.getRut());
        e.setTelefono(d.getTelefono());
        e.setEmail(d.getEmail());
        return repository.save(e);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}