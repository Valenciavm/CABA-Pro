package com.example.CabaPro.Services;

import com.example.CabaPro.models.Tarifa;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Implementación alternativa (modo CSV simple) para generación de reporte.
 * Aplica el principio de Inversión de Dependencias permitiendo nuevos formatos sin tocar la lógica de negocio.
 */
@Service
@Qualifier("excelTarifaReportGenerator")
class ExcelTarifaReportGenerator implements TarifaReportGenerator {

    @Override
    public byte[] generarReporteTotalTarifas(List<Tarifa> tarifas, Long arbitroUsuarioId) throws IOException {
        // Generación simplificada: CSV en memoria. Reemplaza luego con Apache POI.
        StringBuilder sb = new StringBuilder();
        sb.append("ARBITRO_ID,").append("MONTO_TOTAL\n");
        long suma = 0L;
        if (tarifas != null) {
            for (Tarifa t : tarifas) {
                if (t != null && t.getMonto() != null) suma += t.getMonto();
            }
        }
        sb.append(arbitroUsuarioId != null ? arbitroUsuarioId : "-")
          .append(',')
          .append(suma)
          .append('\n');
        // TODO: Implementar versión XLSX con Apache POI si se requiere.
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}
