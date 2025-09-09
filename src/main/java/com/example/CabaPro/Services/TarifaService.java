package com.example.CabaPro.Services;

import org.springframework.stereotype.Service;
import com.example.CabaPro.repositories.TarifaRepository;
import com.example.CabaPro.models.Tarifa;
import com.example.CabaPro.repositories.PartidoArbitroRepository;
import com.example.CabaPro.repositories.PartidoRepository;
import com.example.CabaPro.models.PartidoArbitro;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class TarifaService {

    private final TarifaRepository tarifaRepository;
    private final PartidoArbitroRepository partidoArbitroRepository;
    private final PartidoRepository partidoRepository;

    public TarifaService(TarifaRepository tarifaRepository, PartidoArbitroRepository partidoArbitroRepository,
                         PartidoRepository partidoRepository) {
        this.tarifaRepository = tarifaRepository;
        this.partidoArbitroRepository = partidoArbitroRepository;
        this.partidoRepository = partidoRepository;
    }

    @Transactional
    public void CalcularTarifa(Long partidoId) {
        // Traer la fecha del partido
        LocalDate fechaPartido = null;
        String fechaStr = partidoRepository.findFechaById(partidoId);
        if (fechaStr != null && !fechaStr.isBlank()) {
            try {
                fechaPartido = LocalDate.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException ex) {
                try {
                    fechaPartido = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("d/M/yyyy"));
                } catch (DateTimeParseException ex2) {
                    fechaPartido = null;
                }
            }
        }

        List<PartidoArbitro> asignaciones = partidoArbitroRepository.findByPartidoId(partidoId);

    for (PartidoArbitro asignacion : asignaciones) {

        int montoEspecialidad = 0;
        int montoEscalafon = 0;

        switch (asignacion.getRolPartido()) {
            case "ARBITRO_PRINCIPAL":
                montoEspecialidad = 20;
                break;
            case "AUXILIAR":
                montoEspecialidad = 15;
                break;
            case "SEGUNDO_AUXILIAR":
                montoEspecialidad = 10;
                break;
            default:
                montoEspecialidad = 0;
        }

        switch (asignacion.getArbitro().getEscalafon()) {
            case "Municipal":
                montoEscalafon = 30;
                break;
            case "Nacional":
                montoEscalafon = 20;
                break;
            case "Internacional":
                montoEscalafon = 10;
                break;
            default:
                montoEscalafon = 0;
        }

        int montoTotal = (montoEspecialidad + montoEscalafon) * 10_000;

        // Agregar o actualizar la tarifa
        Tarifa tarifa = tarifaRepository.findByPartidoArbitroId(asignacion.getId())
                .orElseGet(Tarifa::new);

        tarifa.setPartidoArbitro(asignacion);
        tarifa.setMonto(montoTotal);
        tarifa.setFecha(fechaPartido);

        tarifaRepository.save(tarifa);
    }
}


    }

