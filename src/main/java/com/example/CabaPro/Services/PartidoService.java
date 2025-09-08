package com.example.CabaPro.Services;
import com.example.CabaPro.repositories.PartidoRepository;
import com.example.CabaPro.models.Partido;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.CabaPro.repositories.PartidoArbitroRepository;
import com.example.CabaPro.repositories.CanchaRepository;


import java.util.List;
import java.util.Optional;
import com.example.CabaPro.models.PartidoArbitro;
import com.example.CabaPro.models.Arbitro;
import com.example.CabaPro.models.Usuario;
import com.example.CabaPro.repositories.ArbitroRepository;


@Service
public class PartidoService {

    private final PartidoArbitroRepository partidoArbitroRepository;
    private final PartidoRepository repository;
    private final ArbitroRepository arbitroRepository;
    //Inyección de dependencias por constructor 
    public PartidoService(PartidoRepository repository, PartidoArbitroRepository partidoArbitroRepository, ArbitroRepository arbitroRepository) {
        this.repository = repository;
        this.partidoArbitroRepository = partidoArbitroRepository;
        this.arbitroRepository = arbitroRepository;
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

        // permitir que 'resultado' sea opcional: si viene vacío, guardarlo como null
        if (partido.getResultado() != null && partido.getResultado().trim().isEmpty()) {
            partido.setResultado(null);
        }

        try {
            // guardar y devolver la entidad persistida (con id generado)
            return repository.save(partido);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el partido en la base de datos", e);
        }
    }

    @Transactional
    public Partido save(Partido partido, Long principalId, Long auxiliarId, Long segundoAuxId) {
    // Validación: el administrador debe asignar los tres árbitros explícitamente
    if (principalId == null || auxiliarId == null || segundoAuxId == null) {
        throw new IllegalArgumentException("Debe seleccionar los tres árbitros (principal, auxiliar y segundo auxiliar) antes de guardar el partido.");
    }

    // comprobar que los árbitros existen
    Arbitro arbitroPrincipal = arbitroRepository.findById(principalId)
        .orElseThrow(null);
    Arbitro arbitroAuxiliar = arbitroRepository.findById(auxiliarId)
        .orElseThrow(null);
    Arbitro arbitroSegundoAux = arbitroRepository.findById(segundoAuxId)
        .orElseThrow(null);

    // guardar el partido primero para obtener id
    Partido saved = save(partido);

    // crear y guardar las 3 asociaciones con estado PENDIENTE
    PartidoArbitro pa1 = new PartidoArbitro();
    pa1.setPartido(saved);
    pa1.setArbitro(arbitroPrincipal);
    pa1.setRolPartido("ARBITRO_PRINCIPAL");
    pa1.setEstado("PENDIENTE");
    partidoArbitroRepository.save(pa1);

    PartidoArbitro pa2 = new PartidoArbitro();
    pa2.setPartido(saved);
    pa2.setArbitro(arbitroAuxiliar);
    pa2.setRolPartido("AUXILIAR");
    pa2.setEstado("PENDIENTE");
    partidoArbitroRepository.save(pa2);

    PartidoArbitro pa3 = new PartidoArbitro();
    pa3.setPartido(saved);
    pa3.setArbitro(arbitroSegundoAux);
    pa3.setRolPartido("SEGUNDO_AUXILIAR");
    pa3.setEstado("PENDIENTE");
    partidoArbitroRepository.save(pa3);

    return saved;
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
        return true;
    }



    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
