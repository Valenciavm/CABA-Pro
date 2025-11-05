package com.example.CabaPro.Services;

import com.example.CabaPro.DTOs.TarifaDetalleDTO;
import org.springframework.stereotype.Service;
import com.example.CabaPro.repositories.TarifaRepository;
import com.example.CabaPro.models.Tarifa;
import com.example.CabaPro.repositories.PartidoArbitroRepository;
import com.example.CabaPro.repositories.PartidoRepository;
import com.example.CabaPro.models.Partido;
import com.example.CabaPro.models.PartidoArbitro;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.util.Optional;

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
    // Encuentra todas las tarifas del árbitro de aquellos partidos que el árbitro ha aceptado
    @Transactional
    public List<Tarifa> EncontrarTarifas(Long arbitroUsuarioId) {
    if (arbitroUsuarioId == null) return java.util.Collections.emptyList();
     List<Tarifa> tarifas = tarifaRepository.findByPartidoArbitroArbitroUsuarioIdAndPartidoArbitroEstado(arbitroUsuarioId, "ACEPTADO");
     // Filtrar sólo tarifas cuyos partidos ya tengan resultado (partido terminado)
     List<Tarifa> filtradas = new ArrayList<>();
        for (Tarifa t : tarifas) {
         if (t == null) continue;
         PartidoArbitro pa = t.getPartidoArbitro();
         if (pa == null) continue;
         Partido p = pa.getPartido();
         if (p == null) continue;
            // Considerar partido finalizado por el nuevo flag o por tener resultado (compatibilidad)
            if (p.isFinalizado()) {
                filtradas.add(t);
            }
     }
     return filtradas;
        
    }

    @Transactional
    public List<Map<String, Object>> obtenerTarifaDetalles(Long arbitroUsuarioId) {
        List<Tarifa> tarifas = EncontrarTarifas(arbitroUsuarioId);
        List<Map<String,Object>> detalles = new ArrayList<>();
        for (Tarifa t : tarifas) {
            Map<String,Object> detalle = new HashMap<>();
            detalle.put("tarifa", t);
            PartidoArbitro pa = t.getPartidoArbitro();
            String nombrePartido = null;
            String rol = null;
            if (pa != null) {
                rol = pa.getRolPartido();
                if (pa.getPartido() != null) {
                    nombrePartido = pa.getPartido().getNombre();
                }
            }
            detalle.put("partidoNombre", nombrePartido);
            detalle.put("rol", rol);
            detalles.add(detalle);
        }
        return detalles;
    }

    // DEVUELVE DTOs en lugar de entidades para evitar LazyInitializationException al serializar
    @Transactional
    public List<TarifaDetalleDTO> obtenerTarifaDetallesDTO(Long arbitroUsuarioId) {
        List<Tarifa> tarifas = EncontrarTarifas(arbitroUsuarioId);
        List<TarifaDetalleDTO> detalles = new ArrayList<>();
        for (Tarifa t : tarifas) {
            detalles.add(TarifaDetalleDTO.from(t));
        }
        return detalles;
    }

    @Transactional
    public byte[] generarPdfTotalTarifas(Long arbitroUsuarioId) throws IOException {
        List<Tarifa> tarifas = EncontrarTarifas(arbitroUsuarioId);
        long suma = 0L;
        for (Tarifa t : tarifas) {
            if (t != null && t.getMonto() != null) suma += t.getMonto();
        }

        // Crear PDF simple con PDFBox
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);
            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 18);
                cs.newLineAtOffset(50, 700);
                cs.showText("Resumen de Tarifas");
                cs.newLineAtOffset(0, -25);
                cs.setFont(PDType1Font.HELVETICA, 12);
                cs.showText("Arbitro ID: " + (arbitroUsuarioId != null ? arbitroUsuarioId.toString() : "-"));
                cs.newLineAtOffset(0, -18);
                cs.showText("Total a pagar: " + suma);
                cs.endText();
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            doc.save(out);
            return out.toByteArray();
        }
    }
    


    // Calcular las tarifas de los árbitros una vez creados sus partidoos para calcular las tarifas según la asignación que el árbitro haya hecho

    @Transactional
    public void CalcularTarifa(Long partidoId) {
        Optional<Partido> partido = partidoRepository.findById(partidoId);
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
        int pagoPorTorneo = 0;
        if (partido.isPresent() && partido.get().getTorneo() != null) {
            pagoPorTorneo = 10;

        }

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
        

        double montoTotal = (montoEspecialidad + montoEscalafon + pagoPorTorneo) * 10_000;

        // Agregar o actualizar la tarifa
        Tarifa tarifa = tarifaRepository.findByPartidoArbitroId(asignacion.getId())
                .orElseGet(Tarifa::new);

        tarifa.setPartidoArbitro(asignacion);
        tarifa.setMonto((int) montoTotal);
        tarifa.setFecha(fechaPartido);

        tarifaRepository.save(tarifa);
    }
}


    }

