package com.example.CabaPro.models;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Tarifa")
public class Tarifa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTarifa")
    private Integer idTarifa;

    @NotNull
    @Column(name = "monto", nullable = false)
    private Integer monto;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    // FK hacia PartidoArbitro (id único)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partido_arbitro_id", nullable = false)
    private PartidoArbitro partidoArbitro;

    // --- Getters y Setters ---

    public Integer getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(Integer idTarifa) {
        this.idTarifa = idTarifa;
    }

    public Integer getMonto() {
        return monto;
    }

    public void setMonto(Integer monto) {
        this.monto = monto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public PartidoArbitro getPartidoArbitro() {
        return partidoArbitro;
    }

    public void setPartidoArbitro(PartidoArbitro partidoArbitro) {
        this.partidoArbitro = partidoArbitro;
    }
}
