package com.example.CabaPro.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.CabaPro.models.Tarifa;
import com.example.CabaPro.Services.TarifaService;
import com.example.CabaPro.security.CustomUserDetails;
import org.springframework.ui.Model;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.example.CabaPro.models.PartidoArbitro;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


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
    
}
