package com.example.CabaPro.DTOs;
import com.example.CabaPro.models.Usuario;

public class UsuarioDTO {
    private Long id;
    private String username;
    private String email;
    private String nombre;
    private String apellido;
    private String foto;

    public static UsuarioDTO from(Usuario u) {
        if (u == null) return null;
        UsuarioDTO dto = new UsuarioDTO();
        dto.id = u.getId();
        dto.username = u.getUsername();
        dto.email = u.getEmail();
        dto.nombre = u.getNombre();
        dto.apellido = u.getApellido();
        dto.foto = u.getFoto();
        return dto;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getFoto() { return foto; }

    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setFoto(String foto) { this.foto = foto; }



}