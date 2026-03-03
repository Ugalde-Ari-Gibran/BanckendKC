package com.erp.BanckendKC.repository;

import com.erp.BanckendKC.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    
    List<Notificacion> findByAdminDestinatarioOrderByFechaCreacionDesc(String adminDestinatario);
    
    List<Notificacion> findByAdminDestinatarioAndLeidaFalseOrderByFechaCreacionDesc(String adminDestinatario);
    
    long countByAdminDestinatarioAndLeidaFalse(String adminDestinatario);

    List<Notificacion> findByClienteDestinatarioOrderByFechaCreacionDesc(String clienteDestinatario);
    
    List<Notificacion> findByClienteDestinatarioAndLeidaFalseOrderByFechaCreacionDesc(String clienteDestinatario);
    
    long countByClienteDestinatarioAndLeidaFalse(String clienteDestinatario);

    @Modifying
    @Query("UPDATE Notificacion n SET n.leida = true, n.fechaLectura = :fecha WHERE n.adminDestinatario = :adminEmail AND n.leida = false")
    void marcarTodasComoLeidasAdmin(@Param("adminEmail") String adminEmail, @Param("fecha") LocalDateTime fecha);

    @Modifying
    @Query("UPDATE Notificacion n SET n.leida = true, n.fechaLectura = :fecha WHERE n.clienteDestinatario = :clienteEmail AND n.leida = false")
    void marcarTodasComoLeidasCliente(@Param("clienteEmail") String clienteEmail, @Param("fecha") LocalDateTime fecha);
}