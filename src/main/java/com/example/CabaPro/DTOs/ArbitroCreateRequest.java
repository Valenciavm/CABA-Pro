package com.example.CabaPro.DTOs;

public class ArbitroCreateRequest {
    // Campos públicos para acceso directo desde el controller (req.username, etc.)
    public String username;
    public String email;
    public String nombre;
    public String apellido;
    public String password;
    public String escalafon;
    public Boolean disponibilidad;
    public String foto;

    public ArbitroCreateRequest() { }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEscalafon() { return escalafon; }
    public void setEscalafon(String escalafon) { this.escalafon = escalafon; }
    public Boolean getDisponibilidad() { return disponibilidad; }
    public void setDisponibilidad(Boolean disponibilidad) { this.disponibilidad = disponibilidad; }
    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
}
