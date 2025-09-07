package com.example.CabaPro.controllers;



import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PathVariable;


import com.example.CabaPro.models.Cancha;
import com.example.CabaPro.repositories.CanchaRepository;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;

import java.util.List;



@Controller
@RequestMapping("/admin")
public class CanchaController {

    private final CanchaRepository canchaRepository;

    public CanchaController(CanchaRepository canchaRepository){
        this.canchaRepository = canchaRepository;
    }

    @RequestMapping("/cancha") //@RequestMapping
    public String canchas(Model model){
        List<Cancha> canchas = canchaRepository.findAll();
        System.out.println(canchas);
        model.addAttribute("canchas", canchas);
        return "/cancha/canchas";
    }

    @GetMapping("/cancha/nueva")
    public String crearCancha(Model model){
        model.addAttribute("cancha", new Cancha());
        return "/cancha/cancha-form";
    }

    @PostMapping("/cancha/guardar")
    public String guardarCancha(@ModelAttribute Cancha cancha){
        canchaRepository.save(cancha);
        return "redirect:/admin/cancha";
    }

    @GetMapping("/ver-cancha/{id}")
    public String verCancha(@PathVariable("id") Integer id, Model model){
        Cancha cancha = canchaRepository.findById(id).orElse(null);
        model.addAttribute("cancha", cancha);
        return"/cancha/ver-cancha";
    }

    @PostMapping("/eliminar-cancha/{id}")
    public String eliminarCancha(@PathVariable("id") Integer id, Model model){

        Cancha cancha = canchaRepository.findById(id).orElseThrow();
        canchaRepository.delete(cancha);

        return"redirect:/admin/cancha";
    }
    
}
