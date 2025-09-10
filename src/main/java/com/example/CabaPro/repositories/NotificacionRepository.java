// java
package com.example.CabaPro.repositories;

import com.example.CabaPro.models.Notificacion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    // Por usuario (árbitro)
    long countByDestinatarioIdAndLeidaFalse(Long destinatarioId);
    List<Notificacion> findTop20ByDestinatarioIdOrderByFechaCreacionDesc(Long destinatarioId);
    Optional<Notificacion> findByIdAndDestinatarioId(Long id, Long destinatarioId);

    @Modifying
    @Query("update Notificacion n set n.leida = true where n.destinatario.id = :destinatarioId and n.leida = false")
    int marcarTodasComoLeidas(@Param("destinatarioId") Long destinatarioId);

    // Por rol (admin)
    long countByDestinatarioRolAndLeidaFalse(String destinatarioRol);

    @Query("SELECT n FROM Notificacion n WHERE n.destinatarioRol = :rol ORDER BY n.fechaCreacion DESC")
    List<Notificacion> findTopNByDestinatarioRolOrderByFechaCreacionDesc(@Param("rol") String rol, Pageable pageable);

    default List<Notificacion> findTopNByDestinatarioRolOrderByFechaCreacionDesc(String rol, int limit) {
        return findTopNByDestinatarioRolOrderByFechaCreacionDesc(
                rol, org.springframework.data.domain.PageRequest.of(0, limit)
        );
    }
}