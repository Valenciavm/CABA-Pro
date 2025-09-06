package com.example.CabaPro.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.CabaPro.DTOs.RegistroForm;
import com.example.CabaPro.models.Usuario;
import com.example.CabaPro.repositories.UsuarioRepository;

@Controller
public class RegistroController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Muestra el formulario de registro
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("registroForm", new RegistroForm());
        return "registro/registro"; // Corresponde a templates/registro/registro.html
    }

    // Procesa el envío del formulario
    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("registroForm") RegistroForm registroForm, BindingResult result,
            Model model) {

        // Validar que las contraseñas coincidan
        if (!registroForm.getPassword().equals(registroForm.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.registroForm", "Las contraseñas no coinciden");
        }

        // Verificar si el usuario ya existe
        Usuario usuarioExistente = usuarioRepository.findByUsername(registroForm.getUsername());
        if (usuarioExistente != null) {
            result.rejectValue("username", "error.registroForm", "El usuario ya existe");
        }

        // Si hay errores, se regresa al formulario
        if (result.hasErrors()) {
            return "registro/registro";
        }

        // Crear y guardar el nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setUsername(registroForm.getUsername());
        usuario.setNombre(registroForm.getNombre());
        usuario.setApellido(registroForm.getApellido());
        usuario.setEmail(registroForm.getEmail());
        // Encriptar la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(registroForm.getPassword()));
        // Mapear rol según selección: ARBITRO -> ROLE_ARBITRO, ADMIN -> null (pendiente aprobación)
        String tipo = registroForm.getTipoUsuario();
        if ("ARBITRO".equalsIgnoreCase(tipo)) {
            usuario.setRole("ROLE_ARBITRO");
        } else if ("ADMIN".equalsIgnoreCase(tipo)) {
            usuario.setRole(null);
        } else {
            usuario.setRole(null);
        }
        usuarioRepository.save(usuario);

        // Redirigir a la página de login con un parámetro opcional para mostrar mensaje
        // de registro exitoso
        return "redirect:/login?registroExitoso";
    }
    
}
