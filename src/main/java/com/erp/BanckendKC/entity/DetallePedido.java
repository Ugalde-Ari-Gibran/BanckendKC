package com.erp.BanckendKC.entity;

import com.erp.BanckendKC.enums.Presentacion;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "detalles_pedido")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonIgnore
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Presentacion presentacion; // KG, MEDIO_KG, CUARTO_KG

    @Column(nullable = false)
    private Integer cantidad; // cuántas presentaciones

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitarioContado; // precio por kg contado al momento del pedido

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitarioParcial; // precio por kg parcial al momento del pedido

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotalContado;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotalParcial;
}
