package com.erp.BanckendKC.dto.pedido;

import com.erp.BanckendKC.enums.ModalidadEntrega;
import com.erp.BanckendKC.enums.OrigenVenta;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PedidoRequest {

    @NotNull(message = "La modalidad de entrega es obligatoria")
    private ModalidadEntrega modalidad;

    private List<DetallePedidoRequest> detalles;

    private String direccionEntrega;

    private String notas;

    // Para ventas manuales del admin
    private OrigenVenta origenVenta = OrigenVenta.APP_CLIENTE;

    private String clienteNombreManual; // solo en LOCAL_MANUAL

    private String clienteTelefonoManual; // solo en LOCAL_MANUAL

    private Double montoIngreso; // solo en LOCAL_MANUAL para ventas por ingreso directo

    private String metodoPago; // EFECTIVO o TARJETA, solo en LOCAL_MANUAL
}
