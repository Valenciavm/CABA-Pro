package com.example.CabaPro.Services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.CabaPro.models.Usuario;
import com.example.CabaPro.repositories.UsuarioRepository;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario appUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        Set<GrantedAuthority> grantList = new HashSet<>();
        String role = appUser.getRole();
        if (role != null && !role.isBlank()) {
            if (!role.startsWith("ROLE_")) {
                // Normalizar para Spring Security (hasRole("ADMIN") busca "ROLE_ADMIN")
                role = "ROLE_" + role;
            }
            grantList.add(new SimpleGrantedAuthority(role));
            System.out.println("Usuario: " + username + " - Rol asignado: " + role);
        }

        // Aseguramos las flags del usuario (enabled, accountNonExpired, credentialsNonExpired, accountNonLocked)
        return new User(appUser.getUsername(), appUser.getPassword(),
                true, true, true, true, grantList);
    }
}
