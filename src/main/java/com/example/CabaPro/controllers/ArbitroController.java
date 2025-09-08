// src/main/java/com/example/CabaPro/controllers/ArbitroController.java
package com.example.CabaPro.controllers;

import com.example.CabaPro.DTOs.UsuarioPerfilDTO;
import com.example.CabaPro.Services.PartidoService;
import com.example.CabaPro.Services.UsuarioService;
import com.example.CabaPro.Services.ArbitroService;

import com.example.CabaPro.models.PartidoArbitro;
import com.example.CabaPro.models.Arbitro;
import com.example.CabaPro.models.Usuario;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class ArbitroController {

    private final UsuarioService usuarioService;
    private final PartidoService partidoService;


    public ArbitroController(UsuarioService usuarioService, PartidoService partidoService) {
        this.usuarioService = usuarioService;
        this.partidoService = partidoService;

    }

    @GetMapping("/arbitro")
    public String menuArbitro(Model model) {
        return "arbitro/menu_arbitro";
    }

    @GetMapping("/arbitro/perfil")
    public String perfilArbitro(Model model) {
        UsuarioPerfilDTO perfil = usuarioService.obtenerPerfilActual();
        if (!perfil.authenticated) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", perfil.usuario);
        model.addAttribute("usuarioMap", perfil.usuarioMap);
        model.addAttribute("prettyRole", perfil.prettyRole);
        return "arbitro/perfil_arbitro";
    }

    @GetMapping("/arbitro/asignaciones")
    public String verAsignaciones(Authentication authentication, Model model) {
        String currentUsername = authentication.getName();

        // Llama al método corregido, por ejemplo 'findByUsername'
        Usuario usuario = usuarioService.findByUsername(currentUsername)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con nombre: " + currentUsername));

        List<PartidoArbitro> asignaciones = partidoService.findAsignacionesByArbitroId(usuario.getId());

        model.addAttribute("asignaciones", asignaciones);
        return "arbitro/asignaciones-arbitro";
    }

    @PostMapping("/arbitro/asignaciones/{id}/aceptar")
    public String aceptarAsignacion(@PathVariable("id") Long asignacionId, Authentication authentication) {
        Usuario usuario = usuarioService.findUsuario(authentication);
        partidoService.actualizarEstadoAsignacion(asignacionId, usuario, "ACEPTADO");
        return "redirect:/arbitro/asignaciones";
    }

    @PostMapping("/arbitro/asignaciones/{id}/rechazar")
    public String rechazarAsignacion(@PathVariable("id") Long asignacionId, Authentication authentication) {
        Usuario usuario = usuarioService.findUsuario(authentication);
        partidoService.actualizarEstadoAsignacion(asignacionId, usuario, "RECHAZADO");
        return "redirect:/arbitro/asignaciones";
    }

    
    
}