package com.erp.BanckendKC.entity;

import com.erp.BanckendKC.enums.TipoPago;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoPagado;

    @Column(precision = 10, scale = 2)
    private BigDecimal saldoRestante;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPago tipoPago;

    private String notas;

    @Column(updatable = false)
    private LocalDateTime fechaPago;

    private String registradoPor; // nombre del admin que registró

    @PrePersist
    public void prePersist() {
        this.fechaPago = LocalDateTime.now();
    }
}
