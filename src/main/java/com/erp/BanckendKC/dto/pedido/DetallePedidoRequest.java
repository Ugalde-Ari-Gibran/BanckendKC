package com.erp.BanckendKC.dto.pedido;

import com.erp.BanckendKC.enums.Presentacion;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DetallePedidoRequest {

    @NotNull(message = "El producto es obligatorio")
    private Long productoId;

    @NotNull(message = "La presentación es obligatoria")
    private Presentacion presentacion; // KG, MEDIO_KG, CUARTO_KG

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima es 1")
    private Integer cantidad;
}
