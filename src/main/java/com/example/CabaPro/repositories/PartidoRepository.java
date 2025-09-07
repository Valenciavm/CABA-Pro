package com.example.CabaPro.repositories;


import com.example.CabaPro.models.Partido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartidoRepository extends JpaRepository<Partido, Long> {

    Optional<Partido> findById(Long id);

    List<Partido> findAll();
    
    void deleteById(Long id);

}
