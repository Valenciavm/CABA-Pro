package com.example.CabaPro.Services;

import com.example.CabaPro.models.Tarifa;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Implementación en PDF de TarifaReportGenerator usando PDFBox.
 * Forma parte de la aplicación del principio de Inversión de Dependencias.
 */
@Service
@Qualifier("pdfTarifaReportGenerator")
class PdfTarifaReportGenerator implements TarifaReportGenerator {

    @Override
    public byte[] generarReporteTotalTarifas(List<Tarifa> tarifas, Long arbitroUsuarioId) throws IOException {
        long suma = 0L;
        if (tarifas != null) {
            for (Tarifa t : tarifas) {
                if (t != null && t.getMonto() != null) suma += t.getMonto();
            }
        }

        // Crear PDF simple con PDFBox (contenido idéntico al previo)
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
}
