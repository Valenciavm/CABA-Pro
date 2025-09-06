package com.example.CabaPro.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.CabaPro.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsername(String username);
}