package com.erp.BanckendKC.service;

import com.erp.BanckendKC.dto.reporte.ReporteVentasResponse;
import com.erp.BanckendKC.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final PedidoRepository pedidoRepository;

    public ReporteVentasResponse obtenerReporte() {
        LocalDateTime inicioHoy = LocalDate.now().atStartOfDay();
        LocalDateTime finHoy = inicioHoy.plusDays(1);

        LocalDateTime inicioMes = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime finMes = inicioMes.plusMonths(1);

        BigDecimal ventasHoy = pedidoRepository.sumTotalContadoEntreFechas(inicioHoy, finHoy);
        BigDecimal ventasMes = pedidoRepository.sumTotalContadoEntreFechas(inicioMes, finMes);
        BigDecimal saldosPendientes = pedidoRepository.sumSaldosPendientes();

        long pedidosHoy = pedidoRepository.findByFechaHoraBetweenOrderByFechaHoraDesc(inicioHoy, finHoy).size();
        long pedidosMes = pedidoRepository.findByFechaHoraBetweenOrderByFechaHoraDesc(inicioMes, finMes).size();

        return ReporteVentasResponse.builder()
                .totalVentasHoy(ventasHoy != null ? ventasHoy : BigDecimal.ZERO)
                .totalVentasMes(ventasMes != null ? ventasMes : BigDecimal.ZERO)
                .totalSaldosPendientes(saldosPendientes != null ? saldosPendientes : BigDecimal.ZERO)
                .pedidosHoy(pedidosHoy)
                .pedidosMes(pedidosMes)
                .productoMasVendido("N/A") // se puede enriquecer con query más adelante
                .ventasContado(ventasMes != null ? ventasMes : BigDecimal.ZERO)
                .ventasParcial(BigDecimal.ZERO)
                .build();
    }
}
