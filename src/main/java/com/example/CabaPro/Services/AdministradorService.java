package com.example.CabaPro.Services;

import com.example.CabaPro.models.Administrador;
import com.example.CabaPro.models.Usuario;
import com.example.CabaPro.repositories.AdministradorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final UsuarioService usuarioService;

    public AdministradorService(AdministradorRepository administradorRepository,
                                UsuarioService usuarioService) {
        this.administradorRepository = administradorRepository;
        this.usuarioService = usuarioService;
    }

    /**
     * Crea un Administrador (y su Usuario si hace falta).
     * @param username username (puede ser igual al email)
     * @param email
     * @param nombre
     * @param apellido
     * @param rawPassword contraseña en claro (temporal)
     * @param tipo "SUPER_ADMIN" o "ADMIN"
     * @return Administrador existente o recién creado
     */
    @Transactional
    public Administrador createAdminIfNotExists(String username,
                                                String email,
                                                String nombre,
                                                String apellido,
                                                String rawPassword,
                                                String tipo) {
        Optional<Administrador> maybeAdmin = administradorRepository.findByUsuarioEmail(email);
        if (maybeAdmin.isPresent()) {
            return maybeAdmin.get();
        }

        String role = "ROLE_" + tipo; // ej. ROLE_SUPER_ADMIN o ROLE_ADMIN
        Usuario usuario = usuarioService.createUsuarioIfNotExists(username, email, nombre, apellido, rawPassword, role);

        Administrador admin = new Administrador();
        admin.setUsuario(usuario);
        admin.setTipo(tipo);
        return administradorRepository.save(admin);
    }

    @Transactional(readOnly = true)
    public Optional<Administrador> findByUsuarioEmail(String email) {
        return administradorRepository.findByUsuarioEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<Administrador> findByUsuarioUsername(String username) {
        return administradorRepository.findByUsuarioUsername(username);
    }

    @Transactional
    public void deleteAdministrador(Long usuarioId) {
        administradorRepository.deleteById(usuarioId);
    }
}
