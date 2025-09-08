package com.example.CabaPro.repositories;

import com.example.CabaPro.models.Arbitro;
import com.example.CabaPro.models.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ArbitroRepository extends JpaRepository<Arbitro, Long> {
    Optional<Arbitro> findByUsuarioEmail(String email);
    Optional<Arbitro> findByUsuarioUsername(String username);
    Optional<Arbitro> findByUsuario(Usuario usuario);
}
