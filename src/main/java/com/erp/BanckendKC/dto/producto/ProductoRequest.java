package com.erp.BanckendKC.dto.producto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    private String imagenUrl;

    @NotNull(message = "El precio contado es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precioContadoKg;

    @NotNull(message = "El precio parcial es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precioParcialKg;

    @NotNull(message = "La categoría es obligatoria")
    private Long categoriaId;

    private Boolean activo = true;
}
