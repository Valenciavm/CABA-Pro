package com.example.CabaPro.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.CabaPro.Services.PartidoService;
import com.example.CabaPro.models.Partido;
import org.springframework.ui.Model;
import java.util.List;
import java.util.Optional;


//Inyección de dependencias por constructor

@Controller
public class PartidoController {

    private final PartidoService service;

    public PartidoController(PartidoService service) {
        this.service = service;
    }
    

    //Menú principal
    @GetMapping("/partido")
    public String partido(Model model){
       
        return "partido/menu_partido";
    }


    //Para recibir la solicitud GET y mostrar el formulario del pardido
    @GetMapping("/partido/nuevo")
    public String nuevoPartido(Model model){
        model.addAttribute("partido", new Partido());
        return "partido/nuevo_partido";
    }
    
    
    //para recibir el formulario y crear el partido 
    @PostMapping("/partido/nuevo")
    public String crearPartido(@ModelAttribute("partido") Partido partido, RedirectAttributes redirectAttributes,Model model) {
        try {
            //utilizo el servicio para crear el partido
            Partido partidoGuardado = service.save(partido);
            redirectAttributes.addFlashAttribute("mensaje", 
                "Partido  " + partidoGuardado.getNombre()+" creado exitosamente");
            return "redirect:/partido";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("partido", partido);
            return "partido/nuevo_partido";
        }
    }

    //para ver todos los partidos
    @GetMapping("/partido/ver_partidos")
    public String verPartidos(Model model){
       
        List<Partido> partidos = service.findAll();
        model.addAttribute("partidos", partidos);
        return "partido/ver_partidos";
    }
    //para ver un partido
    @GetMapping ("/partido/ver_partido/{id}")
    public String verPartido(Model model,@PathVariable Long id){
        
        Optional<Partido> partido_encontrado= service.findById(id);
        model.addAttribute("partido", partido_encontrado.get());
        return "partido/ver_partido";
    }

    

 //para eliminar un partido
    @PostMapping("/partido/eliminar/{id}")
    public String eliminarPartido(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            service.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje", "Partido eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el partido: " + e.getMessage());
        }
        return "redirect:/partido/ver_partidos";
    }
    
}
