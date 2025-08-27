package com.example.CabaPro.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Usuario")
public class Usuario {
    @Id
    private Integer idUsuario;

    @Column(nullable = false)
    private String rol;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "nombre_usuario", nullable = false, unique = true)
    private String nombreUsuario;

    @Column(nullable = false)
    private String contraseña;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String foto;
}
