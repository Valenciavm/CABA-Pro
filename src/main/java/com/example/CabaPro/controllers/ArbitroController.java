// src/main/java/com/example/CabaPro/controllers/ArbitroController.java
package com.example.CabaPro.controllers;

import com.example.CabaPro.DTOs.UsuarioPerfilDTO;
import com.example.CabaPro.Services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class ArbitroController {

    private final UsuarioService usuarioService;

    public ArbitroController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
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
    
}