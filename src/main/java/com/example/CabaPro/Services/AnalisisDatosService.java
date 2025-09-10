package com.example.CabaPro.Services;

import org.springframework.stereotype.Service;
import com.example.CabaPro.repositories.TarifaRepository;
import com.example.CabaPro.repositories.CanchaRepository;
import com.example.CabaPro.models.Tarifa;
import com.example.CabaPro.models.Partido;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@Service
public class AnalisisDatosService {

	private final PartidoService partidoService;
	private final TarifaRepository tarifaRepository;
	

	public AnalisisDatosService(PartidoService partidoService, TarifaRepository tarifaRepository, CanchaRepository canchaRepository) {
		this.partidoService = partidoService;
		this.tarifaRepository = tarifaRepository;
	}

	/**
	 * Calcula por cada partido creado el gasto (sumas de tarifas + precio de cancha)
	 * y devuelve un mapa con la lista y el total general.
	 */
	public Map<String, Object> calcularGastosPorPartido() {

        
		List<Partido> partidos = partidoService.findAll();
		List<Map<String, Object>> lista = new ArrayList<>();
		long totalGeneral = 0L;

		for (Partido partido : partidos) {
			long sumaTarifas = 0L;
			List<Tarifa> tarifas = tarifaRepository.findByPartidoArbitroPartidoId(partido.getId());
			for (Tarifa tarifa : tarifas) {
				if (tarifa != null && tarifa.getMonto() != null) sumaTarifas += tarifa.getMonto();
			}

			long precioCancha = 0L;
			if (partido.getCancha() != null && partido.getCancha().getPrecio() != null) {
				precioCancha = partido.getCancha().getPrecio();
			}

			long totalPartido = sumaTarifas + precioCancha;
			totalGeneral += totalPartido;

			Map<String, Object> fila = new HashMap<>();
			fila.put("partidoNombre", partido.getNombre());
			fila.put("totalPartido", totalPartido);
			fila.put("partidoId", partido.getId());
			lista.add(fila);
		}

		Map<String, Object> resultado = new HashMap<>();
		resultado.put("gastosPartidos", lista);
		resultado.put("gastoTotal", totalGeneral);
		return resultado;
	}

}
