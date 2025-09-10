package com.example.CabaPro.Services;

import com.example.CabaPro.DTOs.AsignacionPartidoDTO;
import com.example.CabaPro.models.Usuario;
import com.example.CabaPro.repositories.PartidoRepository;
import com.example.CabaPro.models.Partido;
import org.hibernate.Hibernate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.CabaPro.repositories.PartidoArbitroRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import com.example.CabaPro.models.PartidoArbitro;
import com.example.CabaPro.models.Arbitro;
import com.example.CabaPro.models.Cancha;
import com.example.CabaPro.repositories.ArbitroRepository;

@Service
public class PartidoService {

    private final PartidoArbitroRepository partidoArbitroRepository;
    private final PartidoRepository repository; // <- Quien le puso así al repositorio??
    private final ArbitroRepository arbitroRepository;
    private final ArbitroService arbitroService;
    private final CanchaService canchaService;

    public PartidoService(PartidoRepository repository, PartidoArbitroRepository partidoArbitroRepository,
            ArbitroRepository arbitroRepository, ArbitroService arbitroService, CanchaService canchaService) {
        this.repository = repository;
        this.partidoArbitroRepository = partidoArbitroRepository;
        this.arbitroRepository = arbitroRepository;
        this.arbitroService = arbitroService;
        this.canchaService = canchaService;
    }

    @Transactional(readOnly = true)
    public List<AsignacionPartidoDTO> findAllWithAsignaciones() {
        List<Partido> partidos = repository.findAll();
        List<AsignacionPartidoDTO> result = new ArrayList<>();

        for (Partido partido : partidos) {
            List<PartidoArbitro> asignaciones = partidoArbitroRepository.findByPartidoId(partido.getId());
            for (PartidoArbitro pa : asignaciones) {
                Hibernate.initialize(pa.getArbitro());
                if (pa.getArbitro() != null) {
                    Hibernate.initialize(pa.getArbitro().getUsuario());
                }
            }

            result.add(new AsignacionPartidoDTO(partido, asignaciones));
        }

        return result;
    }

    @Transactional(readOnly = true)
    public List<PartidoArbitro> findAsignacionesByArbitroId(Long arbitroUsuarioId) {
        List<PartidoArbitro> asignaciones = partidoArbitroRepository.findByArbitroUsuarioId(arbitroUsuarioId);
        // Inicializar las entidades relacionadas para evitar errores de carga diferida
        // en la vista
        for (PartidoArbitro pa : asignaciones) {
            Hibernate.initialize(pa.getPartido());
            // Añade esta línea para inicializar la cancha
            if (pa.getPartido() != null) {
                Hibernate.initialize(pa.getPartido().getCancha());
            }
        }
        return asignaciones;
    }

    @Transactional
    public void actualizarEstadoAsignacion(Long asignacionId, Usuario usuario, String nuevoEstado) {
        // 1. Buscar la asignación por su ID
        PartidoArbitro asignacion = partidoArbitroRepository.findById(asignacionId)
                .orElseThrow(() -> new NoSuchElementException("Asignación no encontrada con ID: " + asignacionId));

        // 2. Verificar que el usuario actual es el árbitro de esta asignación
        if (!asignacion.getArbitro().getUsuario().getId().equals(usuario.getId())) {
            throw new AccessDeniedException("No tienes permiso para modificar esta asignación.");
        }

        // 3. Verificar que el estado actual es PENDIENTE
        if (!"PENDIENTE".equals(asignacion.getEstado())) {
            throw new IllegalStateException("Solo se pueden modificar asignaciones en estado PENDIENTE.");
        }

        // 4. Actualizar el estado y guardar
        asignacion.setEstado(nuevoEstado);
        partidoArbitroRepository.save(asignacion);
    }

    @Transactional(readOnly = true)
    public Optional<Partido> findById(Long id) {
        return repository.findById(id);
    }

    public List<Partido> findAll() {
        return repository.findAll();
    }

    public Partido save(Partido partido) {
       /* if (!validarDatos(partido)) {
            throw new IllegalArgumentException("Faltan datos obligatorios para crear el partido");
        }*/

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
            throw new IllegalArgumentException(
                    "Debe seleccionar los tres árbitros (principal, auxiliar y segundo auxiliar) antes de guardar el partido.");
        }

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

    // actualizar un partido 
    @Transactional
    public Partido update(Long partidoId,Partido partido, Long principalId, Long auxiliarId, Long segundoAuxId) {
        Partido existingPartido = repository.findById(partidoId)
                .orElseThrow(() -> new NoSuchElementException("Partido no encontrado con ID: " + partidoId));
        // Actualizar los campos del partido existente
        existingPartido.setNombre(partido.getNombre());
        existingPartido.setEquipo1(partido.getEquipo1());
        existingPartido.setEquipo2(partido.getEquipo2());
        existingPartido.setFecha(partido.getFecha());
        existingPartido.setHora(partido.getHora());
        // Actualizar resultado; deferir el guardado del flag 'finalizado' hasta
        // comprobar que todas las asignaciones están aceptadas si se quiere finalizar
        if (partido.getResultado() != null && partido.getResultado().trim().isEmpty()) {
            existingPartido.setResultado(null);
        } else {
            existingPartido.setResultado(partido.getResultado());
        }

        // Actualizar o crear las asignaciones de árbitros para este partido
        List<PartidoArbitro> asignaciones = partidoArbitroRepository.findByPartidoId(partidoId);
        PartidoArbitro principalPa = null;
        PartidoArbitro auxiliarPa = null;
        PartidoArbitro segundoPa = null;
        for (PartidoArbitro pa : asignaciones) {
            if ("ARBITRO_PRINCIPAL".equals(pa.getRolPartido())) principalPa = pa;
            else if ("AUXILIAR".equals(pa.getRolPartido())) auxiliarPa = pa;
            else if ("SEGUNDO_AUXILIAR".equals(pa.getRolPartido())) segundoPa = pa;
        }

        // Principal: sólo actualizar asignación existente si cambia el árbitro
        if (principalId != null && principalPa != null) {
            Arbitro nuevoPrincipal = arbitroRepository.findById(principalId)
                    .orElseThrow(() -> new NoSuchElementException("Árbitro principal no encontrado con ID: " + principalId));
                principalPa.setArbitro(nuevoPrincipal);
                principalPa.setRolPartido("ARBITRO_PRINCIPAL");
                principalPa.setEstado("PENDIENTE");
                partidoArbitroRepository.save(principalPa);
        }

        // Auxiliar
        if (auxiliarId != null && auxiliarPa != null) {
            Arbitro nuevoAuxiliar = arbitroRepository.findById(auxiliarId)
                    .orElseThrow(() -> new NoSuchElementException("Árbitro auxiliar no encontrado con ID: " + auxiliarId));
                auxiliarPa.setArbitro(nuevoAuxiliar);
                auxiliarPa.setRolPartido("AUXILIAR");
                auxiliarPa.setEstado("PENDIENTE");
                partidoArbitroRepository.save(auxiliarPa);
        }

        // Segundo auxiliar
        if (segundoAuxId != null && segundoPa != null) {
            Arbitro nuevoSegundoAux = arbitroRepository.findById(segundoAuxId)
                    .orElseThrow(() -> new NoSuchElementException("Árbitro segundo auxiliar no encontrado con ID: " + segundoAuxId));
                segundoPa.setArbitro(nuevoSegundoAux);
                segundoPa.setRolPartido("SEGUNDO_AUXILIAR");
                segundoPa.setEstado("PENDIENTE");
                partidoArbitroRepository.save(segundoPa);
            
        }

        // Antes de guardar el partido, si el administrador quiere marcarlo como finalizado
        // verificar que todas las asignaciones del partido estén en estado "ACEPTADO".
        boolean quiereFinalizar = partido.isFinalizado();

        if (quiereFinalizar) {
            List<PartidoArbitro> asignacionesActuales = partidoArbitroRepository.findByPartidoId(partidoId);
            boolean todosAceptaron = true;
            for (PartidoArbitro asignacion : asignacionesActuales) {
                if ( !asignacion.getEstado().equals("ACEPTADO")) {
                    todosAceptaron = false;
                    break;
                }
            }
            if (!todosAceptaron) {
                throw new IllegalStateException("No se puede finalizar el partido: todos los árbitros deben haber aceptado sus asignaciones.");
            }
            existingPartido.setFinalizado(true);
        } else {
            existingPartido.setFinalizado(false);
        }

        // Guardar cambios del partido
        repository.save(existingPartido);

        return existingPartido;
    }


    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<Partido> gePartidosByToreno(Partido partido) {
        List<Partido> partidos = repository.findByTorneoId(partido.getId());
        return partidos;
    }

    public List<Partido> gePartidosByTorenoAndFase(Partido partido) {
        List<Partido> partidos = repository.findByTorneoIdAndFase(partido.getId(), partido.getFase());
        return partidos;
    }

    public List<Arbitro> getArbitrosDisponibles() {
        return arbitroService.findAll();
    }

    public List<Cancha> getCanchasDisponibles() {
        return canchaService.findAll();
    }

    public List<Partido> findByTorneoId(Long id){
        List<Partido> partido = repository.findByTorneoId(id);
        return partido;
    }

    @Transactional
    public void reasignarArbitro(Long partidoId, Long nuevoArbitroId, String rol) {
        // 1. Buscar la asignación existente que fue rechazada para este partido y rol.
        PartidoArbitro asignacionAntigua = partidoArbitroRepository.findByPartidoId(partidoId)
                .stream()
                .filter(pa -> pa.getRolPartido().equals(rol))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No se encontró una asignación para el partido con ID " + partidoId + " y rol " + rol));

        // 2. Eliminar la asignación antigua (la que fue rechazada).
        partidoArbitroRepository.delete(asignacionAntigua);

        // 3. Obtener las entidades necesarias para la nueva asignación.
        Partido partido = repository.findById(partidoId)
                .orElseThrow(() -> new NoSuchElementException("Partido no encontrado con ID: " + partidoId));

        Arbitro nuevoArbitro = arbitroRepository.findById(nuevoArbitroId)
                .orElseThrow(() -> new NoSuchElementException("Árbitro no encontrado con ID: " + nuevoArbitroId));

        // 4. Crear la nueva asignación con estado PENDIENTE.
        PartidoArbitro nuevaAsignacion = new PartidoArbitro();
        nuevaAsignacion.setPartido(partido);
        nuevaAsignacion.setArbitro(nuevoArbitro);
        nuevaAsignacion.setRolPartido(rol); // Asigna el rol como String
        nuevaAsignacion.setEstado("PENDIENTE");

        // 5. Guardar la nueva asignación en la base de datos.
        partidoArbitroRepository.save(nuevaAsignacion);
    }
    /*public Partido saveFromDTO(PartidoDTO dto) {
        Partido partido = new Partido();

        // Setear datos básicos
        partido.setNombre(dto.getNombre());
        partido.setEquipo1(dto.getEquipo1());
        partido.setEquipo2(dto.getEquipo2());
        partido.setFecha(dto.getFecha().toString());
        partido.setHora(dto.getHora().toString());

        // Buscar la cancha
        Cancha cancha = canchaService.findById(dto.getCanchaId());
        partido.setCancha(cancha);

        // lista para los árbitros del partido
        List<PartidoArbitro> listaArbitros = new ArrayList<>();
        listaArbitros.add(crearAsignacion(partido, dto.getPrincipalId(), "ARBITRO_PRINCIPAL"));
        listaArbitros.add(crearAsignacion(partido, dto.getAuxiliarId(), "AUXILIAR"));
        listaArbitros.add(crearAsignacion(partido, dto.getSegundoAuxId(), "SEGUNDO_AUXILIAR"));

        // setear la lista en el partido
        partido.setListaArbitros(listaArbitros);

        // validación y guardado
        return save(partido);
    }

    private PartidoArbitro crearAsignacion(Partido partido, Long arbitroId, String rol) {
        Arbitro arbitro = arbitroRepository.findById(arbitroId)
                .orElseThrow(() -> new IllegalArgumentException("Árbitro con id " + arbitroId + " no encontrado"));

        PartidoArbitro pa = new PartidoArbitro();
        pa.setPartido(partido);
        pa.setArbitro(arbitro);
        pa.setRolPartido(rol);
        pa.setEstado("PENDIENTE");
        return pa;
    }*/
}