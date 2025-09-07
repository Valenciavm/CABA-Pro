package com.example.CabaPro.controllers;

import com.example.CabaPro.DTOs.UsuarioPerfilDTO;
import com.example.CabaPro.Services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
public class AdminController {
    private final UsuarioService usuarioService;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);


    public AdminController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/admin")
    public String menuAdmin(Model model) {
        return "admin/menu_admin";
    }

    @GetMapping("/admin/perfil")
    public String perfilAdmin(Model model) {
        UsuarioPerfilDTO perfil = usuarioService.obtenerPerfilActual();
        if (!perfil.authenticated) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", perfil.usuario);
        model.addAttribute("usuarioMap", perfil.usuarioMap);
        model.addAttribute("prettyRole", perfil.prettyRole);
        return "admin/perfil_admin";
    }

    @GetMapping("/admin/partido")
    public String partidosAdmin(Model model){
        return "admin/partido/admin_partidos";
    }

    @GetMapping("/admin/asignaciones")
    public String asignacionesAdmin(Model model){
        return "admin/asignaciones_admin";
    }

    /*@GetMapping("/admin/gestion-arbitros")
    public String gestionArbitros(Model model){
        return "admin/gestion-arbitros";
    }
    */

    @GetMapping("/admin/analisis_arbitros")
    public String analisisArbitros(Model model){
        return "admin/analisis_arbitros";
    }


}