package com.example.CabaPro.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "Cancha")
public class Cancha {

    @Id
    @Column(name = "id_cancha")
    @GeneratedValue(strategy  = GenerationType.IDENTITY)
    private Integer idCancha;

    @NotBlank
    @Column(nullable = false)
    private String tipo;

    @NotBlank
    @Size(min = 10, max = 100)
    @Column(nullable = false)
    private String lugar;

    @NotNull
    @Column(nullable = false)
    private Long precio;

    @NotNull
    @Column(nullable = false)
    private Boolean disponibilidad = true;

    @Column(name = "horario_reserva")
    private LocalDate horarioReserva;

    @OneToOne
    @JoinColumn(name = "partido_id")
    private Partido partido;

    public Cancha(){}

    public Cancha(String tipo, String lugar, Long precio){
        this.tipo = tipo;
        this.lugar = lugar;
        this.precio = precio;
        this.disponibilidad = true;
    }

    // Getters y Setters

    public Integer getIdCancha() {
        return idCancha;
    }

    public void setIdCancha(Integer idCancha) {
        this.idCancha = idCancha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Long getPrecio() {
        return precio;
    }

    public void setPrecio(Long precio) {
        this.precio = precio;
    }

    public Boolean getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(Boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public LocalDate getHorarioReserva() {
        return horarioReserva;
    }

    public void setHorarioReserva(LocalDate horarioReserva) {
        this.horarioReserva = horarioReserva;
    }

    
}
