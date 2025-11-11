package com.example.CabaPro.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.CabaPro.Services.TarifaService;
import com.example.CabaPro.security.CustomUserDetails;
import org.springframework.ui.Model;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import java.io.IOException;

import java.util.Map;
import java.util.List;


@Controller
public class TarifaController {

    private final TarifaService tarifaService;


    public TarifaController(TarifaService tarifaService) {
        this.tarifaService = tarifaService;
    }
     
    @GetMapping("/arbitro/pagos")
    public String ListarPagos(@AuthenticationPrincipal CustomUserDetails user, Model model){
        Long userId = user.getId();
    List<Map<String,Object>> tarifaDetalles = tarifaService.obtenerTarifaDetalles(userId);
    model.addAttribute("tarifaDetalles", tarifaDetalles);

        return "arbitro/pagos";

    }
    
    @GetMapping("/arbitro/tarifas/descargar-total")
    public ResponseEntity<byte[]> descargarTotal(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(name = "format", defaultValue = "pdf") String format) {
        Long userId = user.getId();
        try {
            String fmt = (format == null ? "pdf" : format.trim().toLowerCase());
            byte[] data;
            HttpHeaders headers = new HttpHeaders();
            if ("excel".equals(fmt) || "csv".equals(fmt)) {
                data = tarifaService.generarExcelTotalTarifas(userId);
                headers.setContentType(new MediaType("text", "csv"));
                headers.setContentDispositionFormData("attachment", "total-tarifas.csv");
            } else { 
                data = tarifaService.generarPdfTotalTarifas(userId);
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("attachment", "total-tarifas.pdf");
            }
            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    
}
