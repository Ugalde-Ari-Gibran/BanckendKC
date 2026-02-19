package com.erp.BanckendKC.controller;

import com.erp.BanckendKC.dto.pedido.PedidoRequest;
import com.erp.BanckendKC.dto.pedido.PedidoResponse;
import com.erp.BanckendKC.enums.EstadoPedido;
import com.erp.BanckendKC.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    // Cliente: crear su pedido
    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public ResponseEntity<PedidoResponse> crear(@Valid @RequestBody PedidoRequest request,
                                                  Authentication auth) {
        String email = auth.getName();
        return ResponseEntity.ok(pedidoService.crearPedido(request, email));
    }

    // Cliente: ver sus propios pedidos
    @GetMapping("/mis-pedidos")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<PedidoResponse>> misPedidos(Authentication auth) {
        return ResponseEntity.ok(pedidoService.listarPorCliente(auth.getName()));
    }

    // Admin: ver todos los pedidos
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PedidoResponse>> listarTodos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    // Admin/Cliente: ver detalle de un pedido
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<PedidoResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPorId(id));
    }

    // Admin: actualizar estado del pedido
    @PutMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PedidoResponse> actualizarEstado(@PathVariable Long id,
                                                             @RequestBody Map<String, String> body) {
        EstadoPedido estado = EstadoPedido.valueOf(body.get("estado"));
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, estado));
    }

    // Admin: registrar venta manual del local
    @PostMapping("/manual")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PedidoResponse> ventaManual(@Valid @RequestBody PedidoRequest request,
                                                       Authentication auth) {
        request.setOrigenVenta(com.erp.BanckendKC.enums.OrigenVenta.LOCAL_MANUAL);
        return ResponseEntity.ok(pedidoService.crearPedido(request, null));
    }
}
