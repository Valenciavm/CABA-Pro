package com.example.CabaPro.repositories;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.CabaPro.models.PartidoArbitro;

@Repository
public interface PartidoArbitroRepository extends JpaRepository<PartidoArbitro, Long> {
    // El id del árbitro en la entidad Arbitro se llama "usuarioId", por eso la ruta debe ser arbitro.usuarioId
    Optional<PartidoArbitro> findByPartidoIdAndArbitroUsuarioId(Long partidoId, Long arbitroUsuarioId);
    Optional<PartidoArbitro> findByPartidoId(Long partidoId);

}
