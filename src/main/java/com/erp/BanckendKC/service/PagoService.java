package com.erp.BanckendKC.service;

import com.erp.BanckendKC.dto.pago.PagoRequest;
import com.erp.BanckendKC.entity.Pago;
import com.erp.BanckendKC.entity.Pedido;
import com.erp.BanckendKC.enums.EstadoPago;
import com.erp.BanckendKC.enums.EstadoPedido;
import com.erp.BanckendKC.repository.PagoRepository;
import com.erp.BanckendKC.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;
    private final PedidoRepository pedidoRepository;
    private final NotificacionService notificacionService;

    @Transactional
    public Pago registrarPago(Long pedidoId, PagoRequest request, String adminNombre) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + pedidoId));

        BigDecimal montoPagado = request.getMontoPagado();
        BigDecimal saldoActual = pedido.getSaldoPendiente() != null ? pedido.getSaldoPendiente() : pedido.getTotalContado();

        BigDecimal nuevoSaldo = saldoActual.subtract(montoPagado);
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            nuevoSaldo = BigDecimal.ZERO;
        }

        BigDecimal totalPagado = pedido.getMontoPagado().add(montoPagado);
        pedido.setMontoPagado(totalPagado);
        pedido.setSaldoPendiente(nuevoSaldo);

        if (nuevoSaldo.compareTo(BigDecimal.ZERO) == 0) {
            pedido.setEstadoPago(EstadoPago.LIQUIDADO);
            
            // Cuando se liquida el pedido, automáticamente se marca como entregado
            pedido.setEstado(EstadoPedido.ENTREGADO);
            pedido.setFechaEntrega(LocalDateTime.now());
            
            // Crear notificación de pedido entregado
            notificacionService.crearNotificacionPedidoEntregado(pedido);
        }

        pedidoRepository.save(pedido);

        Pago pago = Pago.builder()
                .pedido(pedido)
                .montoPagado(montoPagado)
                .saldoRestante(nuevoSaldo)
                .tipoPago(request.getTipoPago())
                .notas(request.getNotas())
                .registradoPor(adminNombre)
                .build();

        return pagoRepository.save(pago);
    }

    public List<Pago> obtenerPagos(Long pedidoId) {
        return pagoRepository.findByPedidoIdOrderByFechaPagoDesc(pedidoId);
    }
}
