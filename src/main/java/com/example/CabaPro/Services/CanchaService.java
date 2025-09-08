package com.example.CabaPro.Services;

import org.springframework.stereotype.Service;
import com.example.CabaPro.models.Cancha;
import com.example.CabaPro.repositories.CanchaRepository;
import java.util.List;
@Service
public class CanchaService {

    private final CanchaRepository repository;

    public CanchaService(CanchaRepository repository) {
        this.repository = repository;

    }

    public  List<Cancha> findAll (){

        return repository.findAll();
    }

    

}
