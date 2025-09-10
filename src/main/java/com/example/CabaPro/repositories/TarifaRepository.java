package com.example.CabaPro.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.CabaPro.models.Tarifa;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, Long> {
    java.util.Optional<Tarifa> findByPartidoArbitroId(Long partidoArbitroId);
    java.util.List<Tarifa> findByPartidoArbitroArbitroUsuarioIdAndPartidoArbitroEstado(Long usuarioId, String estado);
    java.util.List<Tarifa> findByPartidoArbitroPartidoId(Long partidoId);
    
}
