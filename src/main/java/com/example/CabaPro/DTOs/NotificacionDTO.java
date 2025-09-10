// src/main/java/com/example/CabaPro/DTOs/NotificacionDTO.java
package com.example.CabaPro.DTOs;

import java.time.Instant;

public class NotificacionDTO {
    public Long id;
    public String tipo;
    public String estado;
    public String mensaje;
    public Instant fechaCreacion;
    public boolean leida;

    public Long partidoId;
    public String partidoNombre;
    public String rolPartido;

    public NotificacionDTO() {}
    public NotificacionDTO(Long id, String tipo, String estado, String mensaje, Instant fechaCreacion, boolean leida,
                           Long partidoId, String partidoNombre, String rolPartido) {
        this.id = id;
        this.tipo = tipo;
        this.estado = estado;
        this.mensaje = mensaje;
        this.fechaCreacion = fechaCreacion;
        this.leida = leida;
        this.partidoId = partidoId;
        this.partidoNombre = partidoNombre;
        this.rolPartido = rolPartido;
    }
}