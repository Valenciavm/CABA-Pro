package com.example.CabaPro.controllers;

import com.example.CabaPro.DTOs.AsignacionPartidoDTO;
import com.example.CabaPro.DTOs.UsuarioPerfilDTO;
import com.example.CabaPro.Services.PartidoService;
import com.example.CabaPro.Services.UsuarioService;
import com.example.CabaPro.models.Arbitro;
import com.example.CabaPro.repositories.ArbitroRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
public class AdminController {
    private final UsuarioService usuarioService;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final PartidoService partidoService;
    private final ArbitroRepository arbitroRepository; // <-- AÑADIR ESTA LÍNEA



    public AdminController(UsuarioService usuarioService,
                           PartidoService partidoService, ArbitroRepository arbitroRepository){
        this.usuarioService = usuarioService;
        this.partidoService = partidoService;
        this.arbitroRepository = arbitroRepository;
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

    // En AdminController.java

    @GetMapping("/admin/asignaciones")
    public String asignacionesAdmin(Model model){
        logger.info("Obteniendo asignaciones de partidos...");
        List<AsignacionPartidoDTO> asignaciones = partidoService.findAllWithAsignaciones();

        List<Arbitro> arbitrosDisponibles = arbitroRepository.findAll();
        model.addAttribute("arbitrosDisponibles", arbitrosDisponibles);
        // --- FIN DE LAS LÍNEAS A AÑADIR ---

        logger.info("Número de partidos con asignaciones: {}", asignaciones.size());
        for (AsignacionPartidoDTO dto : asignaciones) {
            logger.info("Partido: {}, ID: {}, Asignaciones: {}",
                    dto.getPartido().getNombre(),
                    dto.getPartido().getId(),
                    dto.getAsignaciones().size());
        }

        model.addAttribute("asignaciones", asignaciones);
        return "admin/asignaciones_admin";
    }

// ... otras importaciones

    @PostMapping("/admin/partido/reasignar")
    public String reasignarArbitro(@RequestParam("partidoId") Long partidoId,
                                   @RequestParam("nuevoArbitroId") Long nuevoArbitroId,
                                   @RequestParam("rol") String rol,
                                   RedirectAttributes redirectAttributes) {
        try {
            partidoService.reasignarArbitro(partidoId, nuevoArbitroId, rol);
            redirectAttributes.addFlashAttribute("success", "Árbitro reasignado correctamente. La nueva asignación está pendiente de aceptación.");
        } catch (Exception e) {
            logger.error("Error al reasignar árbitro", e);
            redirectAttributes.addFlashAttribute("error", "Error al reasignar el árbitro: " + e.getMessage());
        }
        return "redirect:/admin/asignaciones";
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