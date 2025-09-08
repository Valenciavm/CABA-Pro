package com.example.CabaPro.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "partido")
public class Partido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "equipo1", nullable = false)
    private String equipo1;

    @Column(name = "equipo2", nullable = false)
    private String equipo2;

    @Column(name = "fecha", nullable = false)
    private String fecha;

    @Column(name = "hora", nullable = false)
    private String hora;

    @OneToMany(mappedBy = "partido", cascade = CascadeType.ALL, orphanRemoval =  true, fetch = FetchType.LAZY)
    private List<PartidoArbitro> listaArbitros = new ArrayList<>();



    private String resultado;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEquipo1() {
        return equipo1;
    }

    public String getEquipo2() {
        return equipo2;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getResultado() {
        return resultado;
    }
    public List<PartidoArbitro> getListaArbitros() {
        return listaArbitros;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEquipo1(String equipo1) {
        this.equipo1 = equipo1;
    }

    public void setEquipo2(String equipo2) {
        this.equipo2 = equipo2;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
    public void setListaArbitros(List<PartidoArbitro> listaArbitros) {
        this.listaArbitros = listaArbitros;
    }
}


