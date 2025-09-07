package com.example.CabaPro.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.CabaPro.models.Cancha;

@Repository
public interface CanchaRepository extends  JpaRepository<Cancha, Integer>{
}
