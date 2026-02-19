package com.erp.BanckendKC.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre; // Bistec, Molida, Costilla...

    private String descripcion;

    private String imagenUrl;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioContadoKg; // Precio por kg al contado

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioParcialKg; // Precio por kg en modalidad parcial

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @Column(updatable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    public void prePersist() {
        this.creadoEn = LocalDateTime.now();
    }
}
