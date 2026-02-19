package com.erp.BanckendKC.dto.pedido;

import com.erp.BanckendKC.enums.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PedidoResponse {
    private Long id;
    private Long clienteId;
    private String clienteNombre;
    private String clienteTelefono;
    private List<DetallePedidoResponse> detalles;
    private EstadoPedido estado;
    private ModalidadEntrega modalidad;
    private OrigenVenta origenVenta;
    private EstadoPago estadoPago;
    private BigDecimal totalContado;
    private BigDecimal totalParcial;
    private BigDecimal montoPagado;
    private BigDecimal saldoPendiente;
    private String direccionEntrega;
    private String notas;
    private LocalDateTime fechaHora;
    private LocalDateTime fechaEntrega;
    private String mensajeWhatsApp; // mensaje generado para WhatsApp
}
