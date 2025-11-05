package com.example.CabaPro.DTOs;
import com.example.CabaPro.models.Arbitro;

public class ArbitroDTO {

    private Long usuarioId;
    private String escalafon;
    private Boolean disponibilidad;
    private UsuarioDTO usuario;


    public static ArbitroDTO from(Arbitro a) {
        if (a == null) return null;
        ArbitroDTO dto = new ArbitroDTO();
        dto.usuarioId = a.getUsuarioId();
        dto.escalafon = a.getEscalafon();
        dto.disponibilidad = a.getDisponibilidad();
        dto.usuario = UsuarioDTO.from(a.getUsuario());
        return dto;
    }

    public Long getUsuarioId() { return usuarioId; }
    public String getEscalafon() { return escalafon; }
    public Boolean getDisponibilidad() { return disponibilidad; }
    public UsuarioDTO getUsuario() { return usuario; }

    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public void setEscalafon(String escalafon) { this.escalafon = escalafon; }
    public void setDisponibilidad(Boolean disponibilidad) { this.disponibilidad = disponibilidad; }
    public void setUsuario(UsuarioDTO usuario) { this.usuario = usuario; }

}
