package com.example.CabaPro.DTOs;
import com.example.CabaPro.models.Partido;
import com.example.CabaPro.models.PartidoArbitro;

public class PartidoArbitroDTO { private Long id;
    private String rol;
    private Long partidoId;
    private String partidoNombre;
    private String fecha;
    private String hora;
    private String estado;

    public static PartidoArbitroDTO from(PartidoArbitro pa) {
        if (pa == null) return null;
        PartidoArbitroDTO dto = new PartidoArbitroDTO();
        dto.id = pa.getId();
        dto.rol = pa.getRolPartido();
        dto.estado = pa.getEstado();
        Partido p = pa.getPartido();
        if (p != null) {
            dto.partidoId = p.getId();
            dto.partidoNombre = p.getNombre();
            dto.fecha = p.getFecha();
            dto.hora = p.getHora();
        }
        return dto;
    }

    public Long getId() { return id; }
    public String getRol() { return rol; }
    public String getEstado() { return estado; }
    public Long getPartidoId() { return partidoId; }
    public String getPartidoNombre() { return partidoNombre; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }

    public void setId(Long id) { this.id = id; }
    public void setRol(String rol) { this.rol = rol; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setPartidoId(Long partidoId) { this.partidoId = partidoId; }
    public void setPartidoNombre(String partidoNombre) { this.partidoNombre = partidoNombre; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public void setHora(String hora) { this.hora = hora; }

}