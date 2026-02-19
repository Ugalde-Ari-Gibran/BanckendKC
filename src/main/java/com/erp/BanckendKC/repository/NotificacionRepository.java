package com.erp.BanckendKC.repository;

import com.erp.BanckendKC.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    
    List<Notificacion> findByAdminDestinatarioOrderByFechaCreacionDesc(String adminDestinatario);
    
    List<Notificacion> findByAdminDestinatarioAndLeidaFalseOrderByFechaCreacionDesc(String adminDestinatario);
    
    long countByAdminDestinatarioAndLeidaFalse(String adminDestinatario);

    List<Notificacion> findByClienteDestinatarioOrderByFechaCreacionDesc(String clienteDestinatario);
    
    List<Notificacion> findByClienteDestinatarioAndLeidaFalseOrderByFechaCreacionDesc(String clienteDestinatario);
    
    long countByClienteDestinatarioAndLeidaFalse(String clienteDestinatario);
}