package com.example.CabaPro.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.CabaPro.models.Administrador;
import java.util.Optional;

public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    // Busca por la propiedad anidada usuario.username
    Optional<Administrador> findByUsuarioUsername(String username);
    Optional<Administrador> findByUsuarioEmail(String email);
}
