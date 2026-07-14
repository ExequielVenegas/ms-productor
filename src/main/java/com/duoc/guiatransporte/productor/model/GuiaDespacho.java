package com.duoc.guiatransporte.productor.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "despachos_procesados") // <- Apuntamos a la misma tabla del consumidor
public class GuiaDespacho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long despachoOriginalId;

    private String claveS3;

    private String estadoProcesamiento;
    private LocalDateTime fechaRegistro;
}