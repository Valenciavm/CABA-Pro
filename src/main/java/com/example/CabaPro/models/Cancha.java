package com.example.CabaPro.models;

import jakarta.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "Cancha")
public class Cancha {
    @Id
    @Column(name = "id_cancha")
    private Integer idCancha;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private String lugar;

    @Column(nullable = false)
    private Integer precio;

    @Column(nullable = false)
    private Boolean disponibilidad;

    @Column(name = "horario_reserva")
    private LocalDate horarioReserva;
}
