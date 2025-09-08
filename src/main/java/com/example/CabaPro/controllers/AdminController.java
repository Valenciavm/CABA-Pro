package com.example.CabaPro.controllers;

import com.example.CabaPro.DTOs.AsignacionPartidoDTO;
import com.example.CabaPro.DTOs.UsuarioPerfilDTO;
import com.example.CabaPro.Services.PartidoService;
import com.example.CabaPro.Services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@Controller
public class AdminController {
    private final UsuarioService usuarioService;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final PartidoService partidoService;


    public AdminController(UsuarioService usuarioService, PartidoService partidoService) {
        this.usuarioService = usuarioService;
        this.partidoService = partidoService;
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
        logger.info("Obteniendo asignaciones de partidos...");
        List<AsignacionPartidoDTO> asignaciones = partidoService.findAllWithAsignaciones();

        // Verifica si hay partidos
        logger.info("Número de partidos con asignaciones: {}", asignaciones.size());

        // Examina cada asignación para depurar
        for (AsignacionPartidoDTO dto : asignaciones) {
            logger.info("Partido: {}, ID: {}, Asignaciones: {}",
                    dto.getPartido().getNombre(),
                    dto.getPartido().getId(),
                    dto.getAsignaciones().size());
        }

        model.addAttribute("asignaciones", asignaciones);
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