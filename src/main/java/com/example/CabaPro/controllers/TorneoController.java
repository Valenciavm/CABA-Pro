package com.example.CabaPro.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.CabaPro.Services.TorneoService;
import com.example.CabaPro.models.Torneo;

import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/admin/torneos")
public class TorneoController {

    private final TorneoService torneoService;

    public TorneoController(TorneoService torneoService){
        this.torneoService = torneoService;
    }
    
    @GetMapping("")
    public String verTorneos(Model model){
        List<Torneo> torneos = torneoService.findAll();
        model.addAttribute("torneos", torneos);
        return"torneo/general-torneo";
    }

    @GetMapping("/crear")
    public String crearTorneo(Model model){
        model.addAttribute("torneo", new Torneo());
        return"torneo/torneo-form";
    }

    @PostMapping("/guardar")
    public String guardarTorneo(@ModelAttribute Torneo torneo){

        torneoService.crearTorneo(torneo);

        return "redirect:/admin/torneos/crear";
    }

    @GetMapping("/ver-torneo/{id}")
    public String verTorneo(@PathVariable("id") Long id, Model model){
        Torneo torneo = torneoService.findById(id);
        model.addAttribute("torneo", torneo);
        return"torneo/ver-torneo";
    }

}
