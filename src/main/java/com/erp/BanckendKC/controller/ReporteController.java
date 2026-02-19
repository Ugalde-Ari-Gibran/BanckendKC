package com.erp.BanckendKC.controller;

import com.erp.BanckendKC.dto.reporte.ReporteVentasResponse;
import com.erp.BanckendKC.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping("/ventas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReporteVentasResponse> obtenerReporte() {
        return ResponseEntity.ok(reporteService.obtenerReporte());
    }
}
