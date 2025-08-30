// src/main/java/com/example/CabaPro/controllers/ArbitroController.java
package com.example.CabaPro.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class ArbitroController {

    @GetMapping("/arbitro")
    public String menuArbitro(Model model) {
        return "arbitro/menu_arbitro";
    }
    
}