package com.example.CabaPro.DTOs;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PartidoDTO {

    @NotBlank(message = "El nombre del partido es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String nombre;

    @NotBlank(message = "El nombre del equipo 1 es obligatorio")
    private String equipo1;

    @NotBlank(message = "El nombre del equipo 2 es obligatorio")
    private String equipo2;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Debe seleccionar una fecha")
    private LocalDate fecha;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @NotNull(message = "Debe seleccionar una hora")
    private LocalTime hora;

    @NotNull(message = "Debe seleccionar una cancha")
    private Integer canchaId;

    @NotNull(message = "Debe seleccionar un árbitro principal")
    private Long principalId;

    @NotNull(message = "Debe seleccionar un árbitro auxiliar")
    private Long auxiliarId;

    @NotNull(message = "Debe seleccionar el segundo árbitro auxiliar")
    private Long segundoAuxId;

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCanchaId() {
        return canchaId;
    }

    public void setCanchaId(Integer canchaId) {
        this.canchaId = canchaId;
    }

    public Long getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(Long principalId) {
        this.principalId = principalId;
    }

    public Long getAuxiliarId() {
        return auxiliarId;
    }

    public void setAuxiliarId(Long auxiliarId) {
        this.auxiliarId = auxiliarId;
    }

    public Long getSegundoAuxId() {
        return segundoAuxId;
    }

    public void setSegundoAuxId(Long segundoAuxId) {
        this.segundoAuxId = segundoAuxId;
    }

    public String getEquipo1(){
        return this.equipo1;
    }

    public String getEquipo2(){
        return this.equipo2;
    }

    public LocalTime getHora(){
        return this.hora;
    }

    public LocalDate getFecha(){
        return this.fecha;
    }
}

