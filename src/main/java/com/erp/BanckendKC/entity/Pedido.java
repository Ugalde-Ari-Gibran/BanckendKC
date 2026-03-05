package com.erp.BanckendKC.entity;

import com.erp.BanckendKC.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id")
    private Usuario cliente;

    // Para ventas manuales puede ser null (admin registra el nombre)
    private String clienteNombreManual;

    private String clienteTelefonoManual;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetallePedido> detalles;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModalidadEntrega modalidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrigenVenta origenVenta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estadoPago;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalContado;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalParcial;

    @Column(precision = 10, scale = 2)
    private BigDecimal montoPagado;

    @Column(precision = 10, scale = 2)
    private BigDecimal saldoPendiente;

    private String direccionEntrega;

    private String notas;

    private LocalDateTime fechaHora;

    private LocalDateTime fechaEntrega;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean esCredito = false;

    @PrePersist
    public void prePersist() {
        this.fechaHora = LocalDateTime.now();
        if (this.estado == null) this.estado = EstadoPedido.PENDIENTE;
        if (this.estadoPago == null) this.estadoPago = EstadoPago.PENDIENTE;
        if (this.montoPagado == null) this.montoPagado = BigDecimal.ZERO;
        if (this.esCredito == null) this.esCredito = false;
    }
}
