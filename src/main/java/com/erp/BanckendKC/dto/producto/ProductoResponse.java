package com.erp.BanckendKC.dto.producto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductoResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private String imagenUrl;
    private BigDecimal precioContadoKg;
    private BigDecimal precioParcialKg;
    private Long categoriaId;
    private String categoriaNombre;
    private String categoriaIcono;
    private Boolean activo;

    // Precios precalculados por presentación
    private BigDecimal precioContadoMedioKg;
    private BigDecimal precioContadoCuartoKg;
    private BigDecimal precioParcialMedioKg;
    private BigDecimal precioParcialCuartoKg;
}
