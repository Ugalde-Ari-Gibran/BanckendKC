package com.erp.BanckendKC.repository;

import com.erp.BanckendKC.entity.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    Optional<Factura> findByPedidoId(Long pedidoId);
    Optional<Factura> findByFolio(String folio);
}
