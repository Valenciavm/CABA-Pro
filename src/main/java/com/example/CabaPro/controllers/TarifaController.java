package com.example.CabaPro.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.CabaPro.models.Tarifa;
import com.example.CabaPro.Services.TarifaService;
import com.example.CabaPro.security.CustomUserDetails;
import org.springframework.ui.Model;


import org.springframework.security.core.annotation.AuthenticationPrincipal;



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
        List<Tarifa> tarifas = tarifaService.EncontrarTarifas(userId);
        model.addAttribute("tarifas", tarifas);

        return "arbitro/pagos";

    }
    
}
