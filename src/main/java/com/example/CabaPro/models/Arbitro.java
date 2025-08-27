package com.example.CabaPro.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Arbitro")
public class Arbitro {
    @Id
    @Column(name = "Usuario_idUsuario")
    private Integer usuarioId;

    private String especialidad;
    private String escalafon;

    @Column(nullable = false)
    private Boolean disponibilidad;

    @OneToOne
    @JoinColumn(name = "Usuario_idUsuario", referencedColumnName = "idUsuario", insertable = false, updatable = false)
    private Usuario usuario;
}




