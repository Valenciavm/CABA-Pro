package com.example.CabaPro.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import com.example.CabaPro.repositories.ArbitroRepository;

import com.example.CabaPro.models.Arbitro;
import com.example.CabaPro.models.Cancha;
import com.example.CabaPro.models.Usuario;
import com.example.CabaPro.Services.UsuarioService;
import com.example.CabaPro.Services.ArbitroService;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/admin")
public class GestorArbitroController {

    private final ArbitroRepository arbitroRepository;
    private final ArbitroService arbitroService;

    public GestorArbitroController(ArbitroRepository arbitroRepository, ArbitroService arbitroService){
        this.arbitroRepository = arbitroRepository;
        this.arbitroService = arbitroService;
    }

    @GetMapping("/gestion-arbitros")
    public String gestionArbitros(Model model){
        List<Arbitro> arbitros = arbitroRepository.findAll();
        model.addAttribute("arbitros", arbitros);
        return "admin/gestion-arbitros/general-arbitros";
    }

    @GetMapping("/gestion-arbitros/nuevo")
    public String crearArbitro(Model model){

        Arbitro arbitro = new Arbitro();
        arbitro.setUsuario(new Usuario());
        model.addAttribute("arbitro", arbitro);

        return "admin/gestion-arbitros/arbitro-form";
    }

    @PostMapping("/gestion-arbitros/guardar")
    public String guardarArbitro(Model model, @ModelAttribute("arbitro") Arbitro arbitro){
        arbitroService.createArbitroIfNotExists(arbitro);
        return "redirect:/admin/gestion-arbitros";
    }

    @GetMapping("/gestion-arbitros/ver-arbitro/{id}")
    public String verCancha(@PathVariable("id") Long id, Model model){
        Arbitro arbitro = arbitroRepository.findById(id).orElse(null);
        model.addAttribute("arbitro", arbitro);
        return"admin/gestion-arbitros/ver-arbitro";
    }

    @PostMapping("/gestion-arbitros/eliminar/{id}")
    public String eliminarArbitro(@PathVariable("id") Long id, Model model){
        Arbitro arbitro = arbitroRepository.findById(id).orElse(null);
        arbitroRepository.delete(arbitro);
        return "redirect:/admin/gestion-arbitros";
    }
}