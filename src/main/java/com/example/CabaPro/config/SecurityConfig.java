package com.example.CabaPro.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService usuarioDetailsService;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Rutas públicas
                .requestMatchers("/login", "/registro", "/css/**", "/js/**", "/h2-console/**").permitAll()
                // Rutas protegidas (solo accesibles para usuarios autenticados)
                .requestMatchers("/products/**").authenticated()
                // Rutas específicas según roles
                .requestMatchers("/alumnos/**").hasRole("ADMIN")
                .requestMatchers("/cursos/**").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/products", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            // Configuración adicional para la gestión de sesiones
            .sessionManagement(session -> session
                .maximumSessions(100)
                .maxSessionsPreventsLogin(true)
            )
            // Habilitar protección CSRF
            .csrf((csrf) -> csrf.disable()) // Nota: en producción, mantener CSRF habilitado y configurar adecuadamente.
            // Permitir frames (necesario para la consola H2)
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
        
        return http.build();
    }
    
    // @Autowired
    // @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioDetailsService).passwordEncoder(passwordEncoder());
    }
}