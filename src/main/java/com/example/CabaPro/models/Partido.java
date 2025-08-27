package com.example.CabaPro.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Partido")
public class Partido {
    @Id
    @Column(name = "id_partido")
    private Integer idPartido;

    @Column(nullable = false)
    private String equipos;

    @Column(nullable = false)
    private LocalDate horario;

    @Column(nullable = false)
    private String resultado;

    @ManyToOne
    @JoinColumn(name = "Administrador_Usuario_idUsuario", nullable = false)
    private Administrador administrador;

    @ManyToOne
    @JoinColumn(name = "Cancha_id_cancha", nullable = false)
    private Cancha cancha;

    // getters/setters...
}
