// Java
package com.example.CabaPro.controllers;

import com.example.CabaPro.Services.NotificacionService;
import com.example.CabaPro.models.Notificacion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("/api/admin/notificaciones")
public class AdminNotificacionController {

    private final NotificacionService notificacionService;

    public AdminNotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping("/count")
    public Map<String, Object> count() {
        long count = notificacionService.contarNoLeidasParaAdmin();
        return Map.of("count", count);
    }

    @GetMapping
    public List<Map<String, Object>> list() {
        List<Notificacion> list = notificacionService.listarParaAdmin(50);
        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        List<Map<String, Object>> dto = new ArrayList<>();
        for (Notificacion n : list) {
            dto.add(Map.of(
                    "id", n.getId(),
                    "mensaje", n.getMensaje(),
                    "leida", n.isLeida(),
                    "fechaCreacion", n.getFechaCreacion() != null
                            ? LocalDateTime.ofInstant(n.getFechaCreacion(), ZoneId.systemDefault()).format(fmt)
                            : null
            ));
        }
        return dto;
    }

    @PostMapping("/{id}/leer")
    public ResponseEntity<Void> marcarLeida(@PathVariable Long id) {
        notificacionService.marcarLeida(id);
        return ResponseEntity.ok().build();
    }
}