package com.example.CabaPro.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.CabaPro.Services.PartidoService;
import com.example.CabaPro.Services.ArbitroService;
import com.example.CabaPro.Services.CanchaService;
import com.example.CabaPro.Services.TarifaService;
import com.example.CabaPro.models.Cancha;
import com.example.CabaPro.models.Arbitro;
import com.example.CabaPro.models.Partido;
import org.springframework.ui.Model;
import java.util.List;
import java.util.Optional;


//Inyección de dependencias por constructor

@Controller
public class PartidoController {

    private final PartidoService service;
    private final ArbitroService arbitroService;
    private final CanchaService canchaService;
    private final TarifaService tarifaService;

    public PartidoController(PartidoService service, ArbitroService arbitroService, CanchaService canchaService, TarifaService tarifaService) {
        this.service = service;
        this.arbitroService = arbitroService;
        this.canchaService = canchaService;
        this.tarifaService = tarifaService;
    }
    
    //Para recibir la solicitud GET y mostrar el formulario del pardido
    @GetMapping("admin/partido/nuevo_partido")
    public String nuevoPartido(Model model){
        model.addAttribute("partido", new Partido());
    // pasar lista de arbitros existentes para mostrarlos en el formulario
    List<Arbitro> arbitros = arbitroService.findAll();
    model.addAttribute("arbitros", arbitros);
    List<Cancha> canchas = canchaService.findAll();
    model.addAttribute("canchas", canchas);
    return "admin/partido/nuevo_partido";
}


    //para recibir el formulario y crear el partido
    @PostMapping("admin/partido/nuevo_partido")
    public String crearPartido(@ModelAttribute("partido") Partido partido,
                               Long principalId,
                               Long auxiliarId,
                               Long segundoAuxId,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        try {
            //utilizo el servicio para crear el partido y asignar árbitros
            Partido partidoGuardado = service.save(partido, principalId, auxiliarId, segundoAuxId);
            redirectAttributes.addFlashAttribute("mensaje", 
                "Partido  " + partidoGuardado.getNombre()+" creado exitosamente");
            // Calcular la tarifa para el partido creado
            tarifaService.CalcularTarifa(partidoGuardado.getId());

            return "redirect:/admin/partido";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("partido", partido);
            // volver a cargar la lista de arbitros para que los selects se muestren correctamente
            List<Arbitro> arbitros = arbitroService.findAll();
            model.addAttribute("arbitros", arbitros);
            // preservar las selecciones para que el formulario muestre los valores escogidos
            model.addAttribute("principalId", principalId);
            model.addAttribute("auxiliarId", auxiliarId);
            model.addAttribute("segundoAuxId", segundoAuxId);
            return "admin/partido/nuevo_partido";
        }
    }

    //para ver todos los partidos
    @GetMapping("admin/partido/admin_ver_partidos")
    public String verPartidos(Model model){
       
        List<Partido> partidos = service.findAll();
        model.addAttribute("partidos", partidos);
        return "admin/partido/admin_ver_partidos";
    }
    //para ver un partido
    @GetMapping ("admin/partido/ver_partido/{id}")
    public String verPartido(Model model,@PathVariable Long id){
        
        Optional<Partido> partido_encontrado= service.findById(id);
        model.addAttribute("partido", partido_encontrado.get());
        return "admin/partido/ver_partido";
    }

    

 //para eliminar un partido
    @PostMapping("admin/partido/eliminar/{id}")
    public String eliminarPartido(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            service.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje", "Partido eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el partido: " + e.getMessage());
        }
        return "redirect:/admin/partido/admin_ver_partidos";
    }
    
}
