package com.example.CabaPro.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Arbitro")
public class Arbitro {

    @Id
    @Column(name = "Usuario_idUsuario")
    private Long usuarioId;

    private String especialidad;
    private String escalafon;

    @Column(nullable = false)
    private Boolean disponibilidad;

    @OneToOne
    @MapsId
    @JoinColumn(name = "Usuario_idUsuario", referencedColumnName = "idUsuario")
    private Usuario usuario;

    @OneToMany(mappedBy = "arbitro")
    private List<PartidoArbitro> asignaciones;

    // Getters y setters
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getEscalafon() { return escalafon; }
    public void setEscalafon(String escalafon) { this.escalafon = escalafon; }

    public Boolean getDisponibilidad() { return disponibilidad; }
    public void setDisponibilidad(Boolean disponibilidad) { this.disponibilidad = disponibilidad; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
