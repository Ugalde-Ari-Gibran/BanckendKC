package com.erp.BanckendKC.service;

import com.erp.BanckendKC.dto.pedido.*;
import com.erp.BanckendKC.entity.*;
import com.erp.BanckendKC.enums.*;
import com.erp.BanckendKC.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public PedidoResponse crearPedido(PedidoRequest request, String emailCliente) {
        Usuario cliente = null;
        if (emailCliente != null && request.getOrigenVenta() == OrigenVenta.APP_CLIENTE) {
            cliente = usuarioRepository.findByEmail(emailCliente)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        }

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .clienteNombreManual(request.getClienteNombreManual())
                .clienteTelefonoManual(request.getClienteTelefonoManual())
                .modalidad(request.getModalidad())
                .origenVenta(request.getOrigenVenta() != null ? request.getOrigenVenta() : OrigenVenta.APP_CLIENTE)
                .estado(EstadoPedido.PENDIENTE)
                .estadoPago(EstadoPago.PENDIENTE)
                .direccionEntrega(request.getDireccionEntrega())
                .notas(request.getNotas())
                .montoPagado(BigDecimal.ZERO)
                .build();

        pedido = pedidoRepository.save(pedido);

        // Crear detalles
        BigDecimal totalContado = BigDecimal.ZERO;
        BigDecimal totalParcial = BigDecimal.ZERO;

        final Pedido pedidoFinal = pedido;
        List<DetallePedido> detalles = request.getDetalles().stream().map(d -> {
            Producto producto = productoRepository.findById(d.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + d.getProductoId()));

            BigDecimal factor = BigDecimal.valueOf(d.getPresentacion().toKilos());
            BigDecimal precioContadoUnit = producto.getPrecioContadoKg().multiply(factor).setScale(2, RoundingMode.HALF_UP);
            BigDecimal precioParcialUnit = producto.getPrecioParcialKg().multiply(factor).setScale(2, RoundingMode.HALF_UP);
            BigDecimal cant = BigDecimal.valueOf(d.getCantidad());

            return DetallePedido.builder()
                    .pedido(pedidoFinal)
                    .producto(producto)
                    .presentacion(d.getPresentacion())
                    .cantidad(d.getCantidad())
                    .precioUnitarioContado(precioContadoUnit)
                    .precioUnitarioParcial(precioParcialUnit)
                    .subtotalContado(precioContadoUnit.multiply(cant).setScale(2, RoundingMode.HALF_UP))
                    .subtotalParcial(precioParcialUnit.multiply(cant).setScale(2, RoundingMode.HALF_UP))
                    .build();
        }).collect(Collectors.toList());

        for (DetallePedido d : detalles) {
            totalContado = totalContado.add(d.getSubtotalContado());
            totalParcial = totalParcial.add(d.getSubtotalParcial());
        }

        pedido.setDetalles(detalles);
        pedido.setTotalContado(totalContado);
        pedido.setTotalParcial(totalParcial);
        pedido.setSaldoPendiente(totalContado); // empieza pendiente el total

        pedido = pedidoRepository.save(pedido);

        return toResponse(pedido, generarMensajeWhatsApp(pedido, detalles));
    }

    public List<PedidoResponse> listarTodos() {
        return pedidoRepository.findAll().stream()
                .map(p -> toResponse(p, null))
                .collect(Collectors.toList());
    }

    public List<PedidoResponse> listarPorCliente(String emailCliente) {
        Usuario cliente = usuarioRepository.findByEmail(emailCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return pedidoRepository.findByClienteIdOrderByFechaHoraDesc(cliente.getId()).stream()
                .map(p -> toResponse(p, null))
                .collect(Collectors.toList());
    }

    @Transactional
    public PedidoResponse actualizarEstado(Long id, EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + id));
        pedido.setEstado(nuevoEstado);
        return toResponse(pedidoRepository.save(pedido), null);
    }

    public PedidoResponse obtenerPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + id));
        return toResponse(pedido, null);
    }

    private String generarMensajeWhatsApp(Pedido pedido, List<DetallePedido> detalles) {
        StringBuilder sb = new StringBuilder();
        sb.append("🥩 *Nuevo Pedido - Kamy Carnes*\n\n");

        String clienteNombre = pedido.getCliente() != null
                ? pedido.getCliente().getNombre()
                : pedido.getClienteNombreManual();
        String clienteTel = pedido.getCliente() != null
                ? pedido.getCliente().getTelefono()
                : pedido.getClienteTelefonoManual();

        sb.append("👤 *Cliente:* ").append(clienteNombre).append("\n");
        if (clienteTel != null) sb.append("📞 *Tel:* ").append(clienteTel).append("\n");
        sb.append("\n*Productos:*\n");

        for (DetallePedido d : detalles) {
            String pres = switch (d.getPresentacion()) {
                case KG -> "1 kg";
                case MEDIO_KG -> "½ kg";
                case CUARTO_KG -> "¼ kg";
            };
            sb.append("- ").append(d.getCantidad()).append("x ")
              .append(pres).append(" ").append(d.getProducto().getNombre())
              .append(" = $").append(d.getSubtotalContado()).append("\n");
        }

        sb.append("\n💰 *Total contado:* $").append(pedido.getTotalContado());
        sb.append("\n🚚 *Modalidad:* ").append(
                pedido.getModalidad() == ModalidadEntrega.DOMICILIO ? "Entrega a domicilio" : "Recoger en local");

        if (pedido.getDireccionEntrega() != null) {
            sb.append("\n📍 *Dirección:* ").append(pedido.getDireccionEntrega());
        }

        return sb.toString();
    }

    public PedidoResponse toResponse(Pedido p, String mensajeWhatsApp) {
        List<DetallePedidoResponse> detallesResp = p.getDetalles() == null ? List.of() :
                p.getDetalles().stream().map(d -> DetallePedidoResponse.builder()
                        .id(d.getId())
                        .productoId(d.getProducto().getId())
                        .productoNombre(d.getProducto().getNombre())
                        .categoriaIcono(d.getProducto().getCategoria().getIcono())
                        .presentacion(d.getPresentacion())
                        .cantidad(d.getCantidad())
                        .precioUnitarioContado(d.getPrecioUnitarioContado())
                        .precioUnitarioParcial(d.getPrecioUnitarioParcial())
                        .subtotalContado(d.getSubtotalContado())
                        .subtotalParcial(d.getSubtotalParcial())
                        .build()).collect(Collectors.toList());

        String clienteNombre = p.getCliente() != null ? p.getCliente().getNombre() : p.getClienteNombreManual();
        String clienteTel = p.getCliente() != null ? p.getCliente().getTelefono() : p.getClienteTelefonoManual();

        return PedidoResponse.builder()
                .id(p.getId())
                .clienteId(p.getCliente() != null ? p.getCliente().getId() : null)
                .clienteNombre(clienteNombre)
                .clienteTelefono(clienteTel)
                .detalles(detallesResp)
                .estado(p.getEstado())
                .modalidad(p.getModalidad())
                .origenVenta(p.getOrigenVenta())
                .estadoPago(p.getEstadoPago())
                .totalContado(p.getTotalContado())
                .totalParcial(p.getTotalParcial())
                .montoPagado(p.getMontoPagado())
                .saldoPendiente(p.getSaldoPendiente())
                .direccionEntrega(p.getDireccionEntrega())
                .notas(p.getNotas())
                .fechaHora(p.getFechaHora())
                .fechaEntrega(p.getFechaEntrega())
                .mensajeWhatsApp(mensajeWhatsApp)
                .build();
    }
}
