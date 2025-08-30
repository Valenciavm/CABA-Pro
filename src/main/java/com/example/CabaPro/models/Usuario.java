package com.example.CabaPro.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "Usuario")
public class Usuario {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    @NotBlank
    @Column(nullable = false)
    private String rol;

    @NotBlank
    @Column(nullable = false)
    private String nombre;


    @NotBlank
    //@Pattern(reg)
    @Column(name = "nombre_usuario", nullable = false, unique = true)
    private String nombreUsuario;

    @NotBlank
    @Column(nullable = false)
    private String contraseña;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String correo;

    @Column()// he cambiado esto antes podía ser null
    private String foto;
}
