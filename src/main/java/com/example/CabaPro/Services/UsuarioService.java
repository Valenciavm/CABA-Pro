package com.example.CabaPro.Services;

import com.example.CabaPro.DTOs.UsuarioPerfilDTO;
import com.example.CabaPro.models.Usuario;
import com.example.CabaPro.repositories.UsuarioRepository;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder; // 👈 importa esto
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

    public Optional<Usuario> findByNombre(String username) {
        return usuarioRepository.findByUsername(username);
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
                                            String role,
                                            String foto) {
        Optional<Usuario> existing = usuarioRepository.findByUsername(username);
        if (existing.isPresent()) {
            return existing.get();
        }

        Usuario u = new Usuario();
        u.setUsername(username);
        u.setEmail(email);
        u.setNombre(nombre);
        u.setApellido(apellido);
        u.setFoto(foto);

        // Encriptar la contraseña antes de guardar
        u.setPassword(passwordEncoder.encode(rawPassword));

        u.setRole(role); // p.ej. "ROLE_ADMIN"
        return usuarioRepository.save(u);
    }

    @Transactional
    public Usuario createUsuarioIfNotExists(Usuario usuario, String role) {
        Optional<Usuario> existing = usuarioRepository.findByUsername(usuario.getUsername());
        if (existing.isPresent()) {
            return existing.get();
        }

        usuario.setRole(role);

        // Encriptar la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario findUsuario(Authentication authentication){
        String username = authentication.getName(); // toma el username
        return usuarioRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }


    

    private String prettyRole(String role) {
        if (role == null || role.isBlank()) return "";
        String r = role.startsWith("ROLE_") ? role.substring(5) : role;
        r = r.replace('_', ' ').toLowerCase();
        if (r.isEmpty()) return "";
        return Character.toUpperCase(r.charAt(0)) + r.substring(1);
    }

    public UsuarioPerfilDTO obtenerPerfilActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return new UsuarioPerfilDTO(null, Map.of(), "", false);
        }
        String username = auth.getName();
        Usuario usuario = findByUsername(username).orElse(null);
        if (usuario == null) {
            return new UsuarioPerfilDTO(null, Map.of(), "", true);
        }
        Set<String> excluded = Set.of("class", "password", "id", "username", "email", "nombre", "apellido", "role");
        Map<String, Object> usuarioMap = new LinkedHashMap<>();
        BeanWrapper wrapper = new BeanWrapperImpl(usuario);
        for (PropertyDescriptor pd : wrapper.getPropertyDescriptors()) {
            String name = pd.getName();
            if (excluded.contains(name)) continue;
            try {
                Object val = wrapper.getPropertyValue(name);
                usuarioMap.put(name, val);
            } catch (Exception ignored) { }
        }
        String prettyRole = prettyRole(usuario.getRole());
        return new UsuarioPerfilDTO(usuario, usuarioMap, prettyRole, true);
    }
}
