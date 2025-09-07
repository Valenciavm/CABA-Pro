package com.example.CabaPro.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Administrador")
public class Administrador {

    @Id
    @Column(name = "Usuario_idUsuario")
    private Long usuarioId;

    @OneToOne
    @MapsId // hace que la PK de Administrador sea la PK del Usuario relacionado
    @JoinColumn(name = "Usuario_idUsuario", referencedColumnName = "idUsuario")
    private Usuario usuario;

    @Column(nullable = false)
    private String tipo; // e.g. SUPER_ADMIN, ADMIN

    // Getters y Setters
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
