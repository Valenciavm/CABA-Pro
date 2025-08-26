package com.example.CabaPro.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String Login(Model model) {
        return "login/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        RedirectAttributes redirectAttributes) {
        if ("admin".equals(username) && "admin".equals(password)) {
            return "redirect:/admin";
        } else if ("arbitro".equals(username) && "arbitro".equals(password)) {
            return "redirect:/arbitro";
        } else {
            redirectAttributes.addFlashAttribute("error", "Credenciales incorrectas");
            return "redirect:/login";
        }
    }


}