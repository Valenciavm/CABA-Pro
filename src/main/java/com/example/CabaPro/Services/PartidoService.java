package com.example.CabaPro.Services;
import com.example.CabaPro.repositories.PartidoRepository;
import com.example.CabaPro.models.Partido;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;


@Service
public class PartidoService {

    private final PartidoRepository repository;
    //Inyección de dependencias por constructor 
    public PartidoService(PartidoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Optional<Partido> findById(Long id) {
        return repository.findById(id);
    }

    public List<Partido> findAll() {
        return repository.findAll();
    }

    public Partido save(Partido partido) {
        if (!validarDatos(partido)) {
            throw new IllegalArgumentException("Faltan datos obligatorios para crear el partido");
        }
        
        // Guardar y retornar el partido guardado
        Partido partidoGuardado = repository.save(partido);
        
        // Validar que se guardó correctamente
        if (partidoGuardado.getId() == null) {
            throw new RuntimeException("Error al guardar el partido en la base de datos");
        }
        
        return partidoGuardado;
    }
 //Una validación basiquita
    private boolean validarDatos(Partido partido){
        if(partido.getNombre() == null || partido.getNombre().isEmpty()){
            return false;
        }
        if(partido.getEquipo1() == null || partido.getEquipo1().isEmpty()){
            return false;
        }
        if(partido.getEquipo2() == null || partido.getEquipo2().isEmpty()){
            return false;
        }
        if(partido.getFecha() == null || partido.getFecha().isEmpty()){
            return false;
        }
        if(partido.getHora() == null || partido.getHora().isEmpty()){
        return false;
        }
        if(partido.getResultado() == null || partido.getResultado().isEmpty()){
            return false;
        }
        return true;
    }



    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
