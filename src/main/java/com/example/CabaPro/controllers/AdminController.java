package com.example.CabaPro.controllers;

import com.example.CabaPro.Services.UsuarioService;
import com.example.CabaPro.models.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class AdminController {
    private final UsuarioService usuarioService;

    public AdminController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/admin")
    public String menuAdmin(Model model) {
        return "admin/menu_admin";
    }

    @GetMapping("/admin/perfil")
    public String perfilAdmin(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Usuario usuario = usuarioService.findByUsername(username).orElse(null);
        model.addAttribute("usuario", usuario);
        return "admin/perfil_admin";
    }

    @GetMapping("/admin/partidos")
    public String partidosAdmin(Model model){
        return "admin/partidos_admin";
    }

    @GetMapping("/admin/asignaciones")
    public String asignacionesAdmin(Model model){
        return "admin/asignaciones_admin";
    }

    @GetMapping("/admin/gestion_arbitros")
    public String gestionArbitros(Model model){
        return "admin/gestion_arbitros";
    }

    @GetMapping("/admin/analisis_arbitros")
    public String analisisArbitros(Model model){
        return "admin/analisis_arbitros";
    }






}