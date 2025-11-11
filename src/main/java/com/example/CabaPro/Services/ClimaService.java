package com.example.CabaPro.Services;

import org.asynchttpclient.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class ClimaService {

    private static final String API_URL = "https://open-weather13.p.rapidapi.com/city/fivedaysforcast/6.135/75.340";
    private static final String API_KEY = ""; //TODO: agregar tu API key aquí
    private static final String API_HOST = "open-weather13.p.rapidapi.com";

    public CompletableFuture<Map<String, Object>> obtenerClimaActual() {
    AsyncHttpClient client = new DefaultAsyncHttpClient();

    return CompletableFuture.supplyAsync(() -> {
        try {
            // Llamar a la API
            String responseBody = client
                    .prepare("GET", API_URL)
                    .setHeader("x-rapidapi-key", API_KEY)
                    .setHeader("x-rapidapi-host", API_HOST)
                    .execute()
                    .toCompletableFuture()
                    .thenApply(Response::getResponseBody)
                    .join();

            // Parsear JSON
            JSONObject json = new JSONObject(responseBody);

            // Obtener primer elemento de la lista (clima actual)
            JSONArray list = json.getJSONArray("list");
            JSONObject now = list.getJSONObject(0);

            JSONObject main = now.getJSONObject("main");
            JSONArray weatherArr = now.getJSONArray("weather");
            JSONObject weather = weatherArr.getJSONObject(0);

            // 🔥 Conversión de Kelvin → Celsius
            double tempK = main.getDouble("temp");
            double tempC = tempK - 273.15;
            double tempCRounded = Math.round(tempC * 10.0) / 10.0; // redondear a 1 decimal

            // Crear mapa con datos del clima
            Map<String, Object> climaActual = new HashMap<>();
            climaActual.put("ciudad", json.getJSONObject("city").getString("name"));
            climaActual.put("temperatura", tempCRounded);
            climaActual.put("unidad", "°C");
            climaActual.put("descripcion", weather.getString("description"));
            climaActual.put("humedad", main.getInt("humidity"));

            return climaActual;

        } catch (Exception e) {
            throw new RuntimeException("Error procesando la API de RapidAPI", e);
        } finally {
            try { client.close(); } catch (Exception ignored) {}
        }
    });
    }
}


