package com.example.CabaPro.Services;

import com.example.CabaPro.models.Usuario;
import com.example.CabaPro.repositories.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder; // 👈 importa esto
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor con inyección de dependencias
    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Transactional
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }

    /*
     * Crea un Usuario si no existe (busca por username).
     */
    @Transactional
    public Usuario createUsuarioIfNotExists(String username,
                                            String email,
                                            String nombre,
                                            String apellido,
                                            String rawPassword,
                                            String role) {
        Optional<Usuario> existing = usuarioRepository.findByUsername(username);
        if (existing.isPresent()) {
            return existing.get();
        }

        Usuario u = new Usuario();
        u.setUsername(username);
        u.setEmail(email);
        u.setNombre(nombre);
        u.setApellido(apellido);

        // Encriptar la contraseña antes de guardar
        u.setPassword(passwordEncoder.encode(rawPassword));

        u.setRole(role); // p.ej. "ROLE_ADMIN"
        return usuarioRepository.save(u);
    }
}
