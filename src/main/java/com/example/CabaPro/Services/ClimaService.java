package com.example.CabaPro.Services;

import org.asynchttpclient.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class ClimaService {

    private final String apiUrl;
    private final String apiKey;
    private final String apiHost;

    public ClimaService(
            @Value("${WEATHER_API_URL}") String apiUrl,
            @Value("${WEATHER_API_KEY}") String apiKey,
            @Value("${WEATHER_API_HOST}") String apiHost
    ) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.apiHost = apiHost;
    }

    public CompletableFuture<Map<String, Object>> obtenerClimaActual() {
        AsyncHttpClient client = new DefaultAsyncHttpClient();

        return CompletableFuture.supplyAsync(() -> {
            try {
                String responseBody = client
                        .prepare("GET", apiUrl)
                        .setHeader("x-rapidapi-key", apiKey)
                        .setHeader("x-rapidapi-host", apiHost)
                        .execute()
                        .toCompletableFuture()
                        .thenApply(Response::getResponseBody)
                        .join();

                JSONObject json = new JSONObject(responseBody);

                JSONArray list = json.getJSONArray("list");
                JSONObject now = list.getJSONObject(0);

                JSONObject main = now.getJSONObject("main");
                JSONArray weatherArr = now.getJSONArray("weather");
                JSONObject weather = weatherArr.getJSONObject(0);

                double tempK = main.getDouble("temp");
                double tempC = tempK - 273.15;
                double tempCRounded = Math.round(tempC * 10.0) / 10.0;

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