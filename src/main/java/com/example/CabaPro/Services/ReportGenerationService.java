package com.example.CabaPro.Services;

import com.example.CabaPro.models.Tarifa;

import java.io.IOException;
import java.util.List;

/**
 * Interfaz específica para generación de reportes de Tarifas.
  Implementa el principio de Inversión de Dependencias TarifaService dependerá de esta abstracción en lugar de detalles concretos
*/
interface TarifaReportGenerator {
/*  Metodo abstracto para generar el reporte de tarifas, recibe el ID del árbitro y la lista de tarifas */
    byte[] generarReporteTotalTarifas(List<Tarifa> tarifas, Long arbitroUsuarioId) throws IOException;
}
 
