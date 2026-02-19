package com.erp.BanckendKC.dto.reporte;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ReporteVentasResponse {
    private BigDecimal totalVentasHoy;
    private BigDecimal totalVentasMes;
    private BigDecimal totalSaldosPendientes;
    private Long pedidosHoy;
    private Long pedidosMes;
    private String productoMasVendido;
    private BigDecimal ventasContado;
    private BigDecimal ventasParcial;
}
