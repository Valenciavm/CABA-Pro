package com.example.CabaPro.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.CabaPro.Services.TorneoService;
import com.example.CabaPro.Services.ArbitroService;
import com.example.CabaPro.Services.CanchaService;
import com.example.CabaPro.Services.TarifaService;
import com.example.CabaPro.Services.PartidoService;

import com.example.CabaPro.models.Arbitro;
import com.example.CabaPro.models.Cancha;
import com.example.CabaPro.models.Partido;
import com.example.CabaPro.models.Torneo;

import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/admin/torneos")
public class TorneoController {

    private final TorneoService torneoService;
    private final ArbitroService arbitroService;
    private final CanchaService canchaService;
    private final TarifaService tarifaService;
    private final PartidoService partidoService;

    public TorneoController(TorneoService torneoService, ArbitroService arbitroService, CanchaService canchaService,
            TarifaService tarifaService, PartidoService partidoService) {
        this.torneoService = torneoService;
        this.arbitroService = arbitroService;
        this.canchaService = canchaService;
        this.tarifaService = tarifaService;
        this.partidoService = partidoService;
    }

    @GetMapping("")
    public String verTorneos(Model model) {
        List<Torneo> torneos = torneoService.findAll();
        model.addAttribute("torneos", torneos);
        return "torneo/general-torneo";
    }

    @GetMapping("/crear")
    public String crearTorneo(Model model) {
        model.addAttribute("torneo", new Torneo());
        return "torneo/torneo-form";
    }

    @PostMapping("/guardar")
    public String guardarTorneo(@ModelAttribute Torneo torneo) {

        torneoService.crearTorneo(torneo);

        return "redirect:/admin/torneos/crear";
    }

    /*
     @GetMapping("/ver-torneo/{id}")
    public String verTorneo(@PathVariable("id") Long id, Model model) {
        Torneo torneo = torneoService.findById(id);
        model.addAttribute("torneo", torneo);
        return "torneo/ver-torneo";
    } 
    */

    @GetMapping("/ver-torneo/{id}")
    public String verTorneo2(@PathVariable Long id, Model model) {
        Torneo torneo = torneoService.findById(id);
        List<Partido> partidos = partidoService.findByTorneoId(id);

        // Filtrar por fase
        List<Partido> cuartos = partidos.stream()
                .filter(p -> "Cuartos".equalsIgnoreCase(p.getFase()))
                .toList();

        List<Partido> semis = partidos.stream()
                .filter(p -> "Semifinales".equalsIgnoreCase(p.getFase()))
                .toList();

        List<Partido> finals = partidos.stream()
                .filter(p -> "Final".equalsIgnoreCase(p.getFase()))
                .toList();

        model.addAttribute("torneo", torneo);
        model.addAttribute("cuartos", cuartos);
        model.addAttribute("semis", semis);
        model.addAttribute("final", finals);

        return "torneo/ver-torneo";
    }

    @GetMapping("/ver-torneo/{id}/nuevo-partido")
    public String nuevoPartido(@PathVariable Long id, Model model) {
        model.addAttribute("partido", new Partido());
        List<Arbitro> arbitros = arbitroService.findAll();
        List<Cancha> canchas = canchaService.findAll();
        Torneo torneo = torneoService.findById(id);

        model.addAttribute("arbitros", arbitros);
        model.addAttribute("canchas", canchas);
        model.addAttribute("torneoActual", torneo);
        return "torneo/nuevo-partido-torneo";
    }

    @PostMapping("/ver-torneo/{id}/nuevo-partido")
    public String crearPartido(
            @ModelAttribute("partido") Partido partido,
            Long principalId,
            Long auxiliarId,
            Long segundoAuxId,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            // Validar que el torneo esté presente
            if (partido.getTorneo() == null || partido.getTorneo().getId() == null) {
                throw new IllegalArgumentException("No se ha asignado un torneo al partido");
            }

            // Recuperar el objeto Torneo completo desde la DB
            Torneo torneo = torneoService.findById(partido.getTorneo().getId());
            partido.setTorneo(torneo);

            // Guardar el partido y asignar árbitros
            Partido partidoGuardado = partidoService.save(partido, principalId, auxiliarId, segundoAuxId);

            // Calcular tarifa para el partido creado
            tarifaService.CalcularTarifa(partidoGuardado.getId());

            // Mensaje de éxito
            redirectAttributes.addFlashAttribute("mensaje",
                    "Partido " + partidoGuardado.getNombre() + " creado exitosamente");

            return "redirect:/admin/torneo/ver-torneo/" + torneo.getId();
        } catch (IllegalArgumentException e) {
            // Manejo de errores: recargar lista de árbitros y preservar datos del
            // formulario
            model.addAttribute("error", e.getMessage());
            model.addAttribute("partido", partido);
            model.addAttribute("arbitros", arbitroService.findAll());
            model.addAttribute("principalId", principalId);
            model.addAttribute("auxiliarId", auxiliarId);
            model.addAttribute("segundoAuxId", segundoAuxId);
            return "torneo/nuevo-partido-torneo";
        }
    }

}
