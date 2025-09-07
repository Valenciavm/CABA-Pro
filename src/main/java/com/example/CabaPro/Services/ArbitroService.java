package com.example.CabaPro.Services;

import com.example.CabaPro.models.Arbitro;
import com.example.CabaPro.models.Usuario;
import com.example.CabaPro.repositories.ArbitroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ArbitroService {

    private final ArbitroRepository arbitroRepository;
    private final UsuarioService usuarioService;

    public ArbitroService(ArbitroRepository arbitroRepository,
                          UsuarioService usuarioService) {
        this.arbitroRepository = arbitroRepository;
        this.usuarioService = usuarioService;
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

    @Transactional(readOnly = true)
    public Optional<Arbitro> findByUsuarioEmail(String email) {
        return arbitroRepository.findByUsuarioEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<Arbitro> findByUsuarioUsername(String username) {
        return arbitroRepository.findByUsuarioUsername(username);
    }
}
