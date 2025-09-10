// src/main/java/com/example/CabaPro/controllers/NotificacionController.java
package com.example.CabaPro.controllers;

import com.example.CabaPro.DTOs.NotificacionDTO;
import com.example.CabaPro.Services.NotificacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService service;

    public NotificacionController(NotificacionService service) {
        this.service = service;
    }

    @GetMapping("/count")
    public Map<String, Long> count() {
        return Map.of("count", service.contarNoLeidasUsuarioActual());
    }

    @GetMapping
    public List<NotificacionDTO> listar() {
        return service.listarUsuarioActual();
    }

    @PostMapping("/{id}/leer")
    @Transactional
    public ResponseEntity<Void> marcarLeida(@PathVariable Long id) {
        service.marcarLeida(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/leer-todas")
    @Transactional
    public Map<String, Integer> marcarTodas() {
        int updated = service.marcarTodasLeidasUsuarioActual();
        return Map.of("updated", updated);
    }
}