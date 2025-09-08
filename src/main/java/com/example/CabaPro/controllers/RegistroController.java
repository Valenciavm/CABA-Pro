package com.example.CabaPro.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.CabaPro.DTOs.RegistroForm;
import com.example.CabaPro.Services.RegistroService;


@Controller
public class RegistroController {
 

    @Autowired
    private RegistroService registroService;

    // Muestra el formulario de registro
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        //utilizo el DTO RegistroForm para enlazar los datos del formulario
        model.addAttribute("registroForm", new RegistroForm());
        return "registro/registro"; // Corresponde a templates/registro/registro.html
    }

    // Procesa el envío del formulario
    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("registroForm") RegistroForm registroForm, BindingResult result,
            Model model) {

        // Validar que las contraseñas coincidan
        registroService.ValidarContrasena(registroForm, result);

        // Verificar si el usuario ya existe
        registroService.UsuarioExiste(registroForm, result);

       registroService.crearYGuardarUsuario(registroForm, result, model);

       if (result.hasErrors()) {
           return "registro/registro";
       }

        return "redirect:/login?registroExitoso";
    }
    
}
