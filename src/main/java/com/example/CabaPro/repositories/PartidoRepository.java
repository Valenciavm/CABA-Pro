package com.example.CabaPro.repositories;


import com.example.CabaPro.models.Partido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


// (imports Query, Param removed because only used in custom query already declared above)

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface PartidoRepository extends JpaRepository<Partido, Long> {
    Optional<Partido> findById(Long id);
    List<Partido> findAll();
    void deleteById(Long id);

    @Query("select p.fecha from Partido p where p.id = :id")
    String findFechaById(@Param("id") Long id);

    List<Partido> findByTorneoId(Long torneoId);
    List<Partido> findByTorneoIdAndFase(Long torneoId, String fase);

    // Nuevos métodos para filtrar por administrador creador
    List<Partido> findByAdminId(Long adminId);
    List<Partido> findByAdminIdAndTorneoId(Long adminId, Long torneoId);
}
