package com.erp.BanckendKC.dto.notificacion;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificacionResponse {
    private Long id;
    private String titulo;
    private String mensaje;
    private boolean leida;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaLectura;
    private String tipo;
    private Long pedidoId;
}