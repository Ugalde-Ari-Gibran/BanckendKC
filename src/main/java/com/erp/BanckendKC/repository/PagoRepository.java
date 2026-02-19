package com.erp.BanckendKC.repository;

import com.erp.BanckendKC.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByPedidoIdOrderByFechaPagoDesc(Long pedidoId);
}
