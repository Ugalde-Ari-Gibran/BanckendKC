package com.erp.BanckendKC.service;

import com.erp.BanckendKC.dto.notificacion.NotificacionResponse;
import com.erp.BanckendKC.entity.Notificacion;
import com.erp.BanckendKC.entity.Pedido;
import com.erp.BanckendKC.repository.NotificacionRepository;
import com.erp.BanckendKC.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;

    public void crearNotificacionPedidoNuevo(Pedido pedido) {
        // Obtener todos los admins
        List<String> adminsEmails = usuarioRepository.findByRol(com.erp.BanckendKC.enums.RolUsuario.ADMIN)
                .stream().map(u -> u.getEmail()).collect(Collectors.toList());

        String clienteNombre = pedido.getCliente() != null ? pedido.getCliente().getNombre() : pedido.getClienteNombreManual();
        String titulo = "🛒 Nuevo Pedido #" + pedido.getId();
        String mensaje = String.format("Nuevo pedido de %s por $%.2f - %s",
                clienteNombre,
                pedido.getTotalContado(),
                pedido.getModalidad() == com.erp.BanckendKC.enums.ModalidadEntrega.DOMICILIO ? "Entrega a domicilio" : "Recoger en local");

        // Crear notificación para cada admin
        for (String adminEmail : adminsEmails) {
            Notificacion notificacion = Notificacion.builder()
                    .titulo(titulo)
                    .mensaje(mensaje)
                    .tipo("PEDIDO_NUEVO")
                    .pedidoId(pedido.getId())
                    .adminDestinatario(adminEmail)
                    .build();
            notificacionRepository.save(notificacion);
        }
    }

    public void crearNotificacionPedidoEntregado(Pedido pedido) {
        List<String> adminsEmails = usuarioRepository.findByRol(com.erp.BanckendKC.enums.RolUsuario.ADMIN)
                .stream().map(u -> u.getEmail()).collect(Collectors.toList());

        String clienteNombre = pedido.getCliente() != null ? pedido.getCliente().getNombre() : pedido.getClienteNombreManual();
        String titulo = "✅ Pedido Entregado #" + pedido.getId();
        String mensaje = String.format("El pedido #%d de %s ha sido entregado y liquidado",
                pedido.getId(), clienteNombre);

        for (String adminEmail : adminsEmails) {
            Notificacion notificacion = Notificacion.builder()
                    .titulo(titulo)
                    .mensaje(mensaje)
                    .tipo("PEDIDO_ENTREGADO")
                    .pedidoId(pedido.getId())
                    .adminDestinatario(adminEmail)
                    .build();
            notificacionRepository.save(notificacion);
        }
    }

    public List<NotificacionResponse> obtenerNotificacionesAdmin(String adminEmail) {
        return notificacionRepository.findByAdminDestinatarioOrderByFechaCreacionDesc(adminEmail)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public long contarNoLeidas(String adminEmail) {
        return notificacionRepository.countByAdminDestinatarioAndLeidaFalse(adminEmail);
    }

    public NotificacionResponse marcarComoLeida(Long notificacionId) {
        Notificacion notificacion = notificacionRepository.findById(notificacionId)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
        
        notificacion.setLeida(true);
        notificacion.setFechaLectura(LocalDateTime.now());
        
        return toResponse(notificacionRepository.save(notificacion));
    }

    // Notificaciones para clientes
    public void crearNotificacionCambioEstado(Pedido pedido) {
        if (pedido.getCliente() == null) return;
        
        String titulo = "";
        String mensaje = "";
        
        switch (pedido.getEstado()) {
            case CONFIRMADO:
                titulo = "✅ Pedido Confirmado #" + pedido.getId();
                mensaje = "Tu pedido ha sido confirmado y está en proceso de preparación.";
                break;
            case EN_PREPARACION:
                titulo = "👨‍🍳 Pedido en Preparación #" + pedido.getId();
                mensaje = "Tu pedido está siendo preparado.";
                break;
            case LISTO:
                titulo = "📦 Pedido Listo #" + pedido.getId();
                mensaje = "Tu pedido está listo para recoger o en camino a tu domicilio.";
                break;
            case ENTREGADO:
                titulo = "🎉 Pedido Entregado #" + pedido.getId();
                mensaje = "Tu pedido ha sido entregado exitosamente.";
                break;
            case CANCELADO:
                titulo = "❌ Pedido Cancelado #" + pedido.getId();
                mensaje = "Tu pedido ha sido cancelado.";
                break;
            default:
                return;
        }
        
        Notificacion notificacion = Notificacion.builder()
                .titulo(titulo)
                .mensaje(mensaje)
                .tipo("PEDIDO_CAMBIO_ESTADO")
                .pedidoId(pedido.getId())
                .clienteDestinatario(pedido.getCliente().getEmail())
                .build();
        
        notificacionRepository.save(notificacion);
    }

    public List<NotificacionResponse> obtenerNotificacionesCliente(String clienteEmail) {
        return notificacionRepository.findByClienteDestinatarioOrderByFechaCreacionDesc(clienteEmail)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public long contarNoLeidasCliente(String clienteEmail) {
        return notificacionRepository.countByClienteDestinatarioAndLeidaFalse(clienteEmail);
    }

    private NotificacionResponse toResponse(Notificacion n) {
        return NotificacionResponse.builder()
                .id(n.getId())
                .titulo(n.getTitulo())
                .mensaje(n.getMensaje())
                .leida(n.isLeida())
                .fechaCreacion(n.getFechaCreacion())
                .fechaLectura(n.getFechaLectura())
                .tipo(n.getTipo())
                .pedidoId(n.getPedidoId())
                .build();
    }
}