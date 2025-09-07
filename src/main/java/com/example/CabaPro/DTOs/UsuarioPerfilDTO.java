package com.example.CabaPro.DTOs;
import com.example.CabaPro.models.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import java.beans.PropertyDescriptor;
import java.util.*;

public class UsuarioPerfilDTO {
    public final Usuario usuario;
    public final Map<String, Object> usuarioMap;
    public final String prettyRole;
    public final boolean authenticated;

    public UsuarioPerfilDTO(Usuario usuario, Map<String, Object> usuarioMap, String prettyRole, boolean authenticated) {
        this.usuario = usuario;
        this.usuarioMap = usuarioMap;
        this.prettyRole = prettyRole;
        this.authenticated = authenticated;
    }
}