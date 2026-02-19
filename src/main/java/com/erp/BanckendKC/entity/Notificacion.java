package com.erp.BanckendKC.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, length = 500)
    private String mensaje;

    @Column(nullable = false)
    private boolean leida = false;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column
    private LocalDateTime fechaLectura;

    @Column(nullable = false)
    private String tipo; // PEDIDO_NUEVO, PEDIDO_ENTREGADO, PAGO_REGISTRADO, etc.

    @Column
    private Long pedidoId; // Referencia al pedido relacionado

    @Column
    private String adminDestinatario; // email del admin destinatario

    @Column
    private String clienteDestinatario; // email del cliente destinatario

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
    }
}