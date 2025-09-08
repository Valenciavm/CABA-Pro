package com.example.CabaPro.Services;

import com.example.CabaPro.models.Arbitro;
import com.example.CabaPro.models.PartidoArbitro;
import com.example.CabaPro.models.Usuario;

import com.example.CabaPro.repositories.ArbitroRepository;
import com.example.CabaPro.repositories.PartidoArbitroRepository;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;
import java.util.List;

@Service
public class ArbitroService {

    private final ArbitroRepository arbitroRepository;
    private final PartidoArbitroRepository partidoArbitroRepository;

    private final UsuarioService usuarioService;

    public ArbitroService(ArbitroRepository arbitroRepository,
                          UsuarioService usuarioService, PartidoArbitroRepository partidoArbitroRepository) {
        this.arbitroRepository = arbitroRepository;
        this.usuarioService = usuarioService;
        this.partidoArbitroRepository = partidoArbitroRepository;
    }


    @Transactional
    public Arbitro createArbitroIfNotExists(String username,
                                            String email,
                                            String nombre,
                                            String apellido,
                                            String rawPassword,
                                            String especialidad,
                                            String escalafon,
                                            boolean disponibilidad,
                                            String foto) {
        Optional<Arbitro> maybe = arbitroRepository.findByUsuarioEmail(email);
        if (maybe.isPresent()) {
            return maybe.get();
        }

        Usuario usuario = usuarioService.createUsuarioIfNotExists(username, email, nombre, apellido, rawPassword, "ROLE_ARBITRO", foto);
         // Si el usuario ya existía pero no era arbitro, actualizar su rol

        Arbitro a = new Arbitro();
        a.setUsuario(usuario);
        a.setEspecialidad(especialidad);
        a.setEscalafon(escalafon);
        a.setDisponibilidad(disponibilidad);
        return arbitroRepository.save(a);
    }


    @Transactional
    public Arbitro createArbitroIfNotExists(Arbitro arbitro) {
        Optional<Arbitro> maybe = arbitroRepository.findByUsuarioEmail(arbitro.getUsuario().getEmail());
        if (maybe.isPresent()) {
            return maybe.get();
        }

        Usuario usuario = usuarioService.createUsuarioIfNotExists(arbitro.getUsuario(), "ROLE_ARBITRO"); //QUE HAGO CON LA FOTO??
         // Si el usuario ya existía pero no era arbitro, actualizar su rol

        arbitro.setUsuario(usuario);
        return arbitroRepository.save(arbitro);
    }

    @Transactional
    public void actualizarArbitro(Arbitro arbitroCambios) {

        Arbitro arbitroExistente = arbitroRepository.findById(arbitroCambios.getUsuario().getId()).orElseThrow(() -> new RuntimeException("arbitro no encontrado"));


        // actualizar datos del usuario
        Usuario usuarioExistente = arbitroExistente.getUsuario();
        usuarioExistente.setNombre(arbitroCambios.getUsuario().getNombre());
        usuarioExistente.setApellido(arbitroCambios.getUsuario().getApellido());
        usuarioExistente.setEmail(arbitroCambios.getUsuario().getEmail());

        usuarioExistente.setPassword(usuarioExistente.getPassword());

        arbitroExistente.setEspecialidad(arbitroCambios.getEspecialidad());
        arbitroExistente.setEscalafon(arbitroCambios.getEscalafon());
        arbitroExistente.setDisponibilidad(arbitroCambios.getDisponibilidad());

        arbitroRepository.save(arbitroExistente);
    }

    public List<PartidoArbitro> asignacionesArbitro(Arbitro arbitro) {
        return partidoArbitroRepository.findAllByArbitro(arbitro);
    }

    public Arbitro findByUsuario(Usuario usuario) {
        return arbitroRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Arbitro no encontrado"));
    }


    @Transactional(readOnly = true)
    public Optional<Arbitro> findByUsuarioEmail(String email) {
        return arbitroRepository.findByUsuarioEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<Arbitro> findByUsuarioUsername(String username) {
        return arbitroRepository.findByUsuarioUsername(username);
    }

    @Transactional(readOnly = true)
    public List<Arbitro> findAll() {
        return arbitroRepository.findAll();
    }
}
