package com.example.CabaPro.controllers.api;
import com.example.CabaPro.DTOs.ArbitroCreateRequest;
import com.example.CabaPro.DTOs.ArbitroDTO;
import com.example.CabaPro.DTOs.PartidoArbitroDTO;
import com.example.CabaPro.Services.ArbitroService;
import com.example.CabaPro.Services.PartidoService;
import com.example.CabaPro.models.Arbitro;
import com.example.CabaPro.models.PartidoArbitro;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List; import java.util.Optional; import java.util.stream.Collectors;


@RestController @RequestMapping(value = "/api/arbitros", produces = "application/json")
public class ArbitroRestController {
    private final ArbitroService arbitroService;
    private final PartidoService partidoService;

    public ArbitroRestController(ArbitroService arbitroService, PartidoService partidoService) {
        this.arbitroService = arbitroService;
        this.partidoService = partidoService;
    }

    @GetMapping("/{id}")
    public ArbitroDTO getById(@PathVariable("id") Long id) {
        Optional<Arbitro> maybe = arbitroService.findById(id);
        Arbitro a = maybe.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Árbitro no encontrado"));
        return ArbitroDTO.from(a);
    }

    @GetMapping("/{id}/partidos")
    public List<PartidoArbitroDTO> getAsignaciones(@PathVariable("id") Long id) {
        // id = Usuario_idUsuario del árbitro
        List<PartidoArbitro> asignaciones = partidoService.findAsignacionesByArbitroId(id);
        return asignaciones.stream().map(PartidoArbitroDTO::from).collect(Collectors.toList());
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<ArbitroDTO> create(@RequestBody ArbitroCreateRequest req) {
        if (req == null || req.username == null || req.email == null || req.password == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campos obligatorios: username, email, password");
        }
        Arbitro created = arbitroService.createArbitroIfNotExists(
                req.username,
                req.email,
                req.nombre,
                req.apellido,
                req.password,
                req.escalafon,
                req.disponibilidad != null ? req.disponibilidad : true,
                req.foto
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(ArbitroDTO.from(created));
    }
}
