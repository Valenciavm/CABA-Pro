package com.example.CabaPro.Services;

import com.example.CabaPro.models.Torneo;
import com.example.CabaPro.models.Partido;

import com.example.CabaPro.repositories.TorneoRepository;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TorneoService {

    private final TorneoRepository torneoRepository;

    public TorneoService(TorneoRepository torneoRepository){
        this.torneoRepository = torneoRepository;
    }

    public void crearTorneo(Torneo torneo){

        torneoRepository.save(torneo);

        for(int i = 0; i < torneo.getEquipos().size(); i++){
            Partido partido = new Partido();
            partido.setEquipo1("DSFN");
            partido.setEquipo1("SDSKJFKDFS");
            partido.setTorneo(torneo);
            torneo.addPartido(partido);
        }
    }

    public List<Torneo> findAll(){
        List<Torneo> torneos = torneoRepository.findAll();
        return torneos;
    }

    public Torneo findById(Long id) {
        return torneoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Torneo no encontrado"));
    }

}
