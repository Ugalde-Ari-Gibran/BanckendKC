package com.erp.BanckendKC.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "facturas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    private String rfcCliente;
    private String razonSocialCliente;
    private String regimenFiscal;
    private String codigoPostalFiscal;
    private String usoCfdi;

    @Column(columnDefinition = "TEXT")
    private String xmlDatos; // XML generado internamente (pre-timbrado)

    private String pdfPath; // ruta en OCI Object Storage o local

    private String folio;

    @Column(nullable = false)
    @Builder.Default
    private Boolean timbrada = false; // se timbrará manualmente en PAC

    private LocalDateTime fechaGeneracion;

    private LocalDateTime fechaTimbrado;

    @PrePersist
    public void prePersist() {
        this.fechaGeneracion = LocalDateTime.now();
    }
}
