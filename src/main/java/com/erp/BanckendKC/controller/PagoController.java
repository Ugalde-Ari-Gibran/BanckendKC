package com.erp.BanckendKC.controller;

import com.erp.BanckendKC.dto.pago.PagoRequest;
import com.erp.BanckendKC.entity.Pago;
import com.erp.BanckendKC.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @PostMapping("/{pedidoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Pago> registrarPago(@PathVariable Long pedidoId,
                                               @Valid @RequestBody PagoRequest request,
                                               Authentication auth) {
        return ResponseEntity.ok(pagoService.registrarPago(pedidoId, request, auth.getName()));
    }
}
