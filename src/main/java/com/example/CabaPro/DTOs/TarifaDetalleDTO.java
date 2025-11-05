package com.example.CabaPro.DTOs;
import com.example.CabaPro.models.Partido; import com.example.CabaPro.models.PartidoArbitro; import com.example.CabaPro.models.Tarifa;
import java.time.LocalDate;
public class TarifaDetalleDTO { private Integer tarifaId;
    private Integer monto;
    private LocalDate fecha;
    private Long partidoId;
    private String partidoNombre;
    private String rol;

    public static TarifaDetalleDTO from(Tarifa t) {
        if (t == null) return null;
        TarifaDetalleDTO dto = new TarifaDetalleDTO();
        dto.tarifaId = t.getIdTarifa();
        dto.monto = t.getMonto();
        dto.fecha = t.getFecha();
        PartidoArbitro pa = t.getPartidoArbitro();
        if (pa != null) {
            dto.rol = pa.getRolPartido();
            Partido p = pa.getPartido();
            if (p != null) {
                dto.partidoId = p.getId();
                dto.partidoNombre = p.getNombre();
            }
        }
        return dto;
    }

    public Integer getTarifaId() { return tarifaId; }
    public Integer getMonto() { return monto; }
    public LocalDate getFecha() { return fecha; }
    public Long getPartidoId() { return partidoId; }
    public String getPartidoNombre() { return partidoNombre; }
    public String getRol() { return rol; }

    public void setTarifaId(Integer tarifaId) { this.tarifaId = tarifaId; }
    public void setMonto(Integer monto) { this.monto = monto; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public void setPartidoId(Long partidoId) { this.partidoId = partidoId; }
    public void setPartidoNombre(String partidoNombre) { this.partidoNombre = partidoNombre; }
    public void setRol(String rol) { this.rol = rol; }

}