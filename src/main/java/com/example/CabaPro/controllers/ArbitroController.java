// src/main/java/com/example/CabaPro/controllers/ArbitroController.java
package com.example.CabaPro.controllers;

import com.example.CabaPro.DTOs.UsuarioPerfilDTO;
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


import java.util.List;

@Controller
public class ArbitroController {

    private final UsuarioService usuarioService;
    private final ArbitroService arbitroService;

    public ArbitroController(UsuarioService usuarioService, ArbitroService arbitroService) {
        this.usuarioService = usuarioService;
        this.arbitroService = arbitroService;
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
    public String verAsignaciones(Authentication authentication, Model model){

        Usuario usuario = usuarioService.findUsuario(authentication);

        Arbitro arbitro = arbitroService.findByUsuario(usuario);

        List<PartidoArbitro> asignaciones = arbitroService.asignacionesArbitro(arbitro);
        model.addAttribute("asignaciones", asignaciones);

        return "arbitro/asignaciones-arbitro";
    }

    
    
}