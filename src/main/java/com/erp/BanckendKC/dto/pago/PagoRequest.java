package com.erp.BanckendKC.dto.pago;

import com.erp.BanckendKC.enums.TipoPago;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PagoRequest {

    @NotNull(message = "El monto pagado es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal montoPagado;

    @NotNull(message = "El tipo de pago es obligatorio")
    private TipoPago tipoPago;

    private String notas;
}
