// java
package com.example.CabaPro.models;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "notificacion")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;             // p.ej. "ASIGNACION", "RESPUESTA_ASIGNACION"
    private String estado;           // p.ej. "PENDIENTE", "ACEPTADO", "RECHAZADO"
    @Column(length = 1000)
    private String mensaje;

    @Column(nullable = false)
    private Instant fechaCreacion;

    @Column(nullable = false)
    private boolean leida = false;

    // Destinatario por usuario (para notifs de árbitro)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinatario_id")
    private Usuario destinatario;

    // Emisor opcional
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emisor_id")
    private Usuario emisor;

    // Relación con la asignación (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partido_arbitro_id")
    private PartidoArbitro partidoArbitro;

    // Destinatario por rol (para notifs del admin)
    @Column(name = "destinatario_rol")
    private String destinatarioRol;

    public Long getId() { return id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public Instant getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Instant fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public boolean isLeida() { return leida; }
    public void setLeida(boolean leida) { this.leida = leida; }
    public Usuario getDestinatario() { return destinatario; }
    public void setDestinatario(Usuario destinatario) { this.destinatario = destinatario; }
    public Usuario getEmisor() { return emisor; }
    public void setEmisor(Usuario emisor) { this.emisor = emisor; }
    public PartidoArbitro getPartidoArbitro() { return partidoArbitro; }
    public void setPartidoArbitro(PartidoArbitro partidoArbitro) { this.partidoArbitro = partidoArbitro; }
    public String getDestinatarioRol() { return destinatarioRol; }
    public void setDestinatarioRol(String destinatarioRol) { this.destinatarioRol = destinatarioRol; }
}