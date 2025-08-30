package com.example.CabaPro.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String menuAdmin(Model model) {
        return "admin/menu_admin";
    }

    @GetMapping("/admin/partidos")
    public String partidosAdmin(Model model){
        return "admin/partidos_admin";
    }

}