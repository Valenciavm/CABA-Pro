package com.example.CabaPro.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.CabaPro.models.Usuario;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByRoleIgnoreCase(String role);
}
