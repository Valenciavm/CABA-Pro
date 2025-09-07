package com.example.CabaPro.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.validation.BindingResult;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.CabaPro.DTOs.RegistroForm;
import com.example.CabaPro.models.Usuario;
import com.example.CabaPro.repositories.UsuarioRepository;

@Service 
public class RegistroService {

    @Autowired
    private UsuarioRepository UsuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean ValidarContrasena(RegistroForm registroForm, BindingResult result) {

        String password = registroForm.getPassword();
        String confirmPassword = registroForm.getConfirmPassword();

        if (!password.equals(confirmPassword)) {
            result.rejectValue("confirmPassword", "error.registroForm", "Las contraseñas no coinciden");
            return false;
        }
        return true;
    }

    public boolean UsuarioExiste(RegistroForm registroForm, BindingResult result) {
        String username = registroForm.getUsername();
        if (UsuarioRepository.findByUsername(username).isPresent()) {
            result.rejectValue("username", "error.registroForm", "El usuario ya existe");
            return true;
        }
        return false;
    }

    @Transactional
    public boolean crearYGuardarUsuario(RegistroForm registroForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return false;
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(registroForm.getUsername());
        usuario.setNombre(registroForm.getNombre());
        usuario.setApellido(registroForm.getApellido());
        usuario.setEmail(registroForm.getEmail());
        // Encriptar la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(registroForm.getPassword()));

        String tipo = registroForm.getTipoUsuario();
        if ("ARBITRO".equalsIgnoreCase(tipo)) {
            usuario.setRole("ROLE_ARBITRO");
        } else if ("ADMIN".equalsIgnoreCase(tipo)) {
            usuario.setRole(null);
        } else if ("null".equalsIgnoreCase(tipo)) {
            result.rejectValue("tipoUsuario", "error.registroForm", "Debe seleccionar un tipo de usuario");
            return false;
        }
        UsuarioRepository.save(usuario);
        return true;
    }









    
}
