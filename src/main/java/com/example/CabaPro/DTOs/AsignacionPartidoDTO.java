package com.example.CabaPro.DTOs;

import com.example.CabaPro.models.Partido;
import com.example.CabaPro.models.PartidoArbitro;
import java.util.List;
import java.util.Optional;

public class AsignacionPartidoDTO {
    private Partido partido;
    private List<PartidoArbitro> asignaciones;

    public AsignacionPartidoDTO(Partido partido, List<PartidoArbitro> asignaciones) {
        this.partido = partido;
        this.asignaciones = asignaciones;
    }

    // Getters y setters
    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    public List<PartidoArbitro> getAsignaciones() {
        return asignaciones;
    }

    public void setAsignaciones(List<PartidoArbitro> asignaciones) {
        this.asignaciones = asignaciones;
    }
}