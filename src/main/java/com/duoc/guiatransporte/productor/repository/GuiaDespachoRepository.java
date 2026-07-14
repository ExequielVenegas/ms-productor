package com.duoc.guiatransporte.productor.repository;

import com.duoc.guiatransporte.productor.model.GuiaDespacho;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GuiaDespachoRepository extends JpaRepository<GuiaDespacho, Long> {
    Optional<GuiaDespacho> findByDespachoOriginalId(Long despachoOriginalId);
}