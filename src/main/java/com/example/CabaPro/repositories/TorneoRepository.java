package com.example.CabaPro.repositories;

import com.example.CabaPro.models.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TorneoRepository  extends JpaRepository<Torneo, Long>  {
    

}
