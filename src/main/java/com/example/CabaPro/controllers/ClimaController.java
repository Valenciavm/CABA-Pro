package com.example.CabaPro.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


import com.example.CabaPro.Services.ClimaService;

@Controller
public class ClimaController {
    
    @GetMapping("/admin/clima")
    public String mostrarClimaAdmin() {
        return "clima/clima";
    }

    @GetMapping("/arbitro/clima")
    public String mostrarClima() {
        return "clima/clima";
    }
}
