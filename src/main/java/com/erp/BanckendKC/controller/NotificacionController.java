package com.erp.BanckendKC.controller;

import com.erp.BanckendKC.dto.notificacion.NotificacionResponse;
import com.erp.BanckendKC.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificacionResponse>> obtenerNotificaciones(Authentication auth) {
        return ResponseEntity.ok(notificacionService.obtenerNotificacionesAdmin(auth.getName()));
    }

    @GetMapping("/no-leidas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> contarNoLeidas(Authentication auth) {
        return ResponseEntity.ok(notificacionService.contarNoLeidas(auth.getName()));
    }

    @PutMapping("/{id}/leer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NotificacionResponse> marcarComoLeida(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.marcarComoLeida(id));
    }

    @PutMapping("/leer-todas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> marcarTodasComoLeidasAdmin(Authentication auth) {
        notificacionService.marcarTodasComoLeidasAdmin(auth.getName());
        return ResponseEntity.noContent().build();
    }

    // Endpoints para clientes
    @GetMapping("/cliente")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public ResponseEntity<List<NotificacionResponse>> obtenerNotificacionesCliente(Authentication auth) {
        return ResponseEntity.ok(notificacionService.obtenerNotificacionesCliente(auth.getName()));
    }

    @GetMapping("/cliente/no-leidas")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public ResponseEntity<Long> contarNoLeidasCliente(Authentication auth) {
        return ResponseEntity.ok(notificacionService.contarNoLeidasCliente(auth.getName()));
    }

    @PutMapping("/cliente/{id}/leer")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public ResponseEntity<NotificacionResponse> marcarComoLeidaCliente(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.marcarComoLeida(id));
    }

    @PutMapping("/cliente/leer-todas")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public ResponseEntity<Void> marcarTodasComoLeidasCliente(Authentication auth) {
        notificacionService.marcarTodasComoLeidasCliente(auth.getName());
        return ResponseEntity.noContent().build();
    }
}