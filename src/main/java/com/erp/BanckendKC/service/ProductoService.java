package com.erp.BanckendKC.service;

import com.erp.BanckendKC.dto.producto.ProductoRequest;
import com.erp.BanckendKC.dto.producto.ProductoResponse;
import com.erp.BanckendKC.entity.Categoria;
import com.erp.BanckendKC.entity.Producto;
import com.erp.BanckendKC.repository.CategoriaRepository;
import com.erp.BanckendKC.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public List<ProductoResponse> listarActivos() {
        return productoRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ProductoResponse> listarPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaIdAndActivoTrue(categoriaId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProductoResponse obtenerPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
        return toResponse(producto);
    }

    public ProductoResponse crear(ProductoRequest request) {
        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + request.getCategoriaId()));

        Producto producto = Producto.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .imagenUrl(request.getImagenUrl())
                .precioContadoKg(request.getPrecioContadoKg())
                .precioParcialKg(request.getPrecioParcialKg())
                .categoria(categoria)
                .activo(request.getActivo() != null ? request.getActivo() : true)
                .build();

        return toResponse(productoRepository.save(producto));
    }

    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + request.getCategoriaId()));

        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setImagenUrl(request.getImagenUrl());
        producto.setPrecioContadoKg(request.getPrecioContadoKg());
        producto.setPrecioParcialKg(request.getPrecioParcialKg());
        producto.setCategoria(categoria);
        if (request.getActivo() != null) producto.setActivo(request.getActivo());

        return toResponse(productoRepository.save(producto));
    }

    public void desactivar(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    public ProductoResponse toResponse(Producto p) {
        BigDecimal half = BigDecimal.valueOf(0.5);
        BigDecimal quarter = BigDecimal.valueOf(0.25);

        return ProductoResponse.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .descripcion(p.getDescripcion())
                .imagenUrl(p.getImagenUrl())
                .precioContadoKg(p.getPrecioContadoKg())
                .precioParcialKg(p.getPrecioParcialKg())
                .categoriaId(p.getCategoria().getId())
                .categoriaNombre(p.getCategoria().getNombre())
                .categoriaIcono(p.getCategoria().getIcono())
                .activo(p.getActivo())
                // Precios precalculados
                .precioContadoMedioKg(p.getPrecioContadoKg().multiply(half).setScale(2, RoundingMode.HALF_UP))
                .precioContadoCuartoKg(p.getPrecioContadoKg().multiply(quarter).setScale(2, RoundingMode.HALF_UP))
                .precioParcialMedioKg(p.getPrecioParcialKg().multiply(half).setScale(2, RoundingMode.HALF_UP))
                .precioParcialCuartoKg(p.getPrecioParcialKg().multiply(quarter).setScale(2, RoundingMode.HALF_UP))
                .build();
    }
}
