package com.example.CabaPro.controllers;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.CabaPro.Services.ClimaService;

import java.util.Map;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/clima")
public class ClimaController {

    @Autowired
    private ClimaService climaService;

    @GetMapping
    public CompletableFuture<ResponseEntity<Map<String, Object>>> obtenerClimaMedellin() {
        return climaService.obtenerClimaActual()
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex ->
                        ResponseEntity.internalServerError()
                                .body(Map.of("error", ex.getMessage())));
    }
}
