package com.duoc.guiatransporte.productor.dto;

import com.duoc.guiatransporte.productor.model.Transportista;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransportistaDTO {
    private Long id;
    private String nombre;
    private String rut;
    private String telefono;
    private String email;

    public TransportistaDTO(Transportista t) {
        this.id = t.getId();
        this.nombre = t.getNombre();
        this.rut = t.getRut();
        this.telefono = t.getTelefono();
        this.email = t.getEmail();
    }
}