// java
package com.example.CabaPro.Services;

import com.example.CabaPro.DTOs.NotificacionDTO;
import com.example.CabaPro.models.*;
import com.example.CabaPro.repositories.NotificacionRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class NotificacionService {

    private final NotificacionRepository repo;
    private final UsuarioService usuarioService;

    public NotificacionService(NotificacionRepository repo, UsuarioService usuarioService) {
        this.repo = repo;
        this.usuarioService = usuarioService;
    }

    // java
    @Transactional
    public void crearNotificacionAsignacion(PartidoArbitro pa) {
        if (pa == null || pa.getId() == null) return; // asegurar que está persistida
        if (pa.getArbitro() == null || pa.getArbitro().getUsuario() == null) return;

        Partido partido = pa.getPartido();
        String nombrePartido = partido != null ? partido.getNombre() : "Partido";
        String fecha = partido != null ? String.valueOf(partido.getFecha()) : "";
        String hora = partido != null ? String.valueOf(partido.getHora()) : "";
        String rol = pa.getRolPartido() != null ? pa.getRolPartido().replace('_', ' ') : "";

        String msg = "Nueva asignación: " + nombrePartido +
                (fecha.isEmpty() ? "" : " • " + fecha) +
                (hora.isEmpty() ? "" : " • " + hora) +
                (rol.isEmpty() ? "" : " • Rol: " + rol);

        Notificacion n = new Notificacion();
        n.setTipo("ASIGNACION");
        n.setEstado(pa.getEstado());
        n.setMensaje(msg);
        n.setFechaCreacion(java.time.Instant.now());
        n.setLeida(false);
        n.setDestinatario(pa.getArbitro().getUsuario());
        n.setEmisor(null);
        n.setPartidoArbitro(pa); // pa ya persistido
        repo.save(n);
    }

    @Transactional(readOnly = true)
    public long contarNoLeidasUsuarioActual() {
        Usuario u = usuarioService.obtenerPerfilActual().usuario;
        if (u == null) return 0;
        return repo.countByDestinatarioIdAndLeidaFalse(u.getId());
    }

    @Transactional(readOnly = true)
    public List<NotificacionDTO> listarUsuarioActual() {
        Usuario u = usuarioService.obtenerPerfilActual().usuario;
        if (u == null) return List.of();

        return repo.findTop20ByDestinatarioIdOrderByFechaCreacionDesc(u.getId())
                .stream()
                .map(n -> {
                    PartidoArbitro pa = n.getPartidoArbitro();
                    Partido p = pa != null ? pa.getPartido() : null;
                    if (p != null) Hibernate.initialize(p);
                    return new NotificacionDTO(
                            n.getId(),
                            n.getTipo(),
                            n.getEstado(),
                            n.getMensaje(),
                            n.getFechaCreacion(),
                            n.isLeida(),
                            p != null ? p.getId() : null,
                            p != null ? p.getNombre() : null,
                            pa != null ? pa.getRolPartido() : null
                    );
                })
                .toList();
    }

    @Transactional
    public void marcarLeida(Long notificacionId) {
        Usuario u = usuarioService.obtenerPerfilActual().usuario;
        if (u == null) return;
        repo.findByIdAndDestinatarioId(notificacionId, u.getId()).ifPresent(n -> {
            if (!n.isLeida()) {
                n.setLeida(true);
                repo.save(n);
            }
        });
    }

    @Transactional
    public int marcarTodasLeidasUsuarioActual() {
        Usuario u = usuarioService.obtenerPerfilActual().usuario;
        if (u == null) return 0;
        return repo.marcarTodasComoLeidas(u.getId());
    }

    // Notificación para ADMIN cuando el árbitro responde la asignación
    @Transactional
    public void crearNotificacionRespuestaAsignacion(PartidoArbitro pa) {
        String partido = pa.getPartido() != null ? pa.getPartido().getNombre() : "";
        String rol = pa.getRolPartido() != null ? pa.getRolPartido() : "";
        String arbitro = (pa.getArbitro() != null && pa.getArbitro().getUsuario() != null)
                ? pa.getArbitro().getUsuario().getNombre() + " " + pa.getArbitro().getUsuario().getApellido()
                : "Árbitro";
        String estado = pa.getEstado();
        String mensaje = arbitro + " " + (estado != null ? estado.toLowerCase() : "actualizó")
                + " la asignación (" + rol + ") del partido " + partido + ".";

        Notificacion n = new Notificacion();
        n.setTipo("RESPUESTA_ASIGNACION");
        n.setEstado(estado);
        n.setMensaje(mensaje);
        n.setFechaCreacion(Instant.now());
        n.setLeida(false);
        n.setDestinatarioRol("ADMIN");
        n.setPartidoArbitro(pa);

        repo.save(n);
    }

    @Transactional(readOnly = true)
    public long contarNoLeidasParaAdmin() {
        return repo.countByDestinatarioRolAndLeidaFalse("ADMIN");
    }

    @Transactional(readOnly = true)
    public List<Notificacion> listarParaAdmin(int limit) {
        return repo.findTopNByDestinatarioRolOrderByFechaCreacionDesc("ADMIN", limit);
    }
}