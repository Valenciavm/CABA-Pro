package com.example.CabaPro.models;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;

@Entity
@Table(name = "Partido_Arbitro")
public class PartidoArbitro {   

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // si el id es autoincremental
    @Column(name = "partido_arbitro_id")
    private Long id;

    @NotNull
    @Column(name = "rol_partido", nullable = false)
    private String rolPartido;

    @NotNull
    @Column(name = "estado", nullable = false)
    private String estado;

    // Relación con Partido
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Partido_id_partido", nullable = false)
    private Partido partido;

    // Relación con Arbitro
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Arbitro_Usuario_idUsuario", nullable = false)
    private Arbitro arbitro;

    // Getters y setters...
}
