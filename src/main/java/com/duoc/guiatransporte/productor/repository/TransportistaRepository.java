package com.duoc.guiatransporte.productor.repository;

import com.duoc.guiatransporte.productor.model.Transportista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportistaRepository extends JpaRepository<Transportista, Long> {
}