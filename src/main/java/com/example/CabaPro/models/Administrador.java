package com.example.CabaPro.models;

import jakarta.persistence.*;


@Entity
@Table(name = "Administrador")
public class Administrador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Usuario_idUsuario")
    private Integer usuarioId;

    @Column(nullable = false)
    private String tipo;

    @OneToOne
    @JoinColumn(name = "Usuario_idUsuario", referencedColumnName = "idUsuario", insertable = false, updatable = false)
    private Usuario usuario;
}
