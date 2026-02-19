package com.erp.BanckendKC.dto.pedido;

import com.erp.BanckendKC.enums.Presentacion;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DetallePedidoResponse {
    private Long id;
    private Long productoId;
    private String productoNombre;
    private String categoriaIcono;
    private Presentacion presentacion;
    private Integer cantidad;
    private BigDecimal precioUnitarioContado;
    private BigDecimal precioUnitarioParcial;
    private BigDecimal subtotalContado;
    private BigDecimal subtotalParcial;
}
