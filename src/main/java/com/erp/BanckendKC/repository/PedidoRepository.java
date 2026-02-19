package com.erp.BanckendKC.repository;

import com.erp.BanckendKC.entity.Pedido;
import com.erp.BanckendKC.enums.EstadoPago;
import com.erp.BanckendKC.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteIdOrderByFechaHoraDesc(Long clienteId);

    List<Pedido> findByEstadoOrderByFechaHoraDesc(EstadoPedido estado);

    List<Pedido> findByEstadoPagoOrderByFechaHoraDesc(EstadoPago estadoPago);

    List<Pedido> findByFechaHoraBetweenOrderByFechaHoraDesc(LocalDateTime inicio, LocalDateTime fin);

    // Pedidos abiertos (no entregados)
    List<Pedido> findByEstadoNotAndEstadoNotOrderByFechaHoraDesc(EstadoPedido estado1, EstadoPedido estado2);

    // Pedidos cerrados (entregados) por rango de fechas
    List<Pedido> findByEstadoAndFechaEntregaBetweenOrderByFechaEntregaDesc(EstadoPedido estado, LocalDateTime inicio, LocalDateTime fin);

    @Query("SELECT SUM(p.totalContado) FROM Pedido p WHERE p.fechaHora BETWEEN :inicio AND :fin AND p.estadoPago = 'LIQUIDADO'")
    BigDecimal sumTotalContadoEntreFechas(LocalDateTime inicio, LocalDateTime fin);

    @Query("SELECT SUM(p.saldoPendiente) FROM Pedido p WHERE p.estadoPago = 'PENDIENTE'")
    BigDecimal sumSaldosPendientes();
}
