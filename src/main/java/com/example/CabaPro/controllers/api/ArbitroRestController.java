package com.example.CabaPro.controllers.api;

import com.example.CabaPro.DTOs.ArbitroCreateRequest;
import com.example.CabaPro.DTOs.ArbitroDTO;
import com.example.CabaPro.DTOs.PartidoArbitroDTO;
import com.example.CabaPro.DTOs.TarifaDetalleDTO;
import com.example.CabaPro.Services.ArbitroService;
import com.example.CabaPro.Services.PartidoService;
import com.example.CabaPro.Services.TarifaService;
import com.example.CabaPro.models.Arbitro;
import com.example.CabaPro.models.PartidoArbitro;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/arbitros", produces = "application/json")
public class ArbitroRestController {

    private final ArbitroService arbitroService;
    private final PartidoService partidoService;
    private final TarifaService tarifaService;

    public ArbitroRestController(
            ArbitroService arbitroService,
            PartidoService partidoService,
            TarifaService tarifaService
    ) {
        this.arbitroService = arbitroService;
        this.partidoService = partidoService;
        this.tarifaService = tarifaService;
    }

    // GET /api/arbitros/{id} -> JSON o 404 sin HTML
    @GetMapping("/{id}")
    public ResponseEntity<ArbitroDTO> getById(@PathVariable("id") Long id) {
        Optional<Arbitro> maybe = arbitroService.findById(id);
        return maybe
                .map(a -> ResponseEntity.ok(ArbitroDTO.from(a)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // GET /api/arbitros/{id}/partidos -> lista JSON
    @GetMapping("/{id}/partidos")
    public List<PartidoArbitroDTO> getAsignaciones(@PathVariable("id") Long id) {
        // id = Usuario_idUsuario del árbitro
        List<PartidoArbitro> asignaciones = partidoService.findAsignacionesByArbitroId(id);
        return asignaciones.stream().map(PartidoArbitroDTO::from).collect(Collectors.toList());
    }


    // GET /api/arbitros/{id}/pagos -> lista JSON (DTO plano) sin LazyInitializationException
    @GetMapping("/{id}/pagos")
    public List<TarifaDetalleDTO> listarPagos(@PathVariable("id") Long id) {
        return tarifaService.obtenerTarifaDetallesDTO(id);
    }

    // GET /api/arbitros/{id}/pagos/descargar-total -> PDF
    @GetMapping(value = "/{id}/pagos/descargar-total", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> descargarTotal(@PathVariable("id") Long id) {
        try {
            byte[] pdf = tarifaService.generarPdfTotalTarifas(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header("Content-Disposition", "attachment; filename=total-tarifas.pdf")
                    .body(pdf);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // POST /api/arbitros -> crea árbitro y devuelve su JSON
    @PostMapping(consumes = "application/json")
    public ResponseEntity<ArbitroDTO> create(@RequestBody ArbitroCreateRequest req) {
        if (req == null || req.username == null || req.email == null || req.password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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
