package com.example.CabaPro.config;

import com.example.CabaPro.config.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.core.annotation.Order;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService usuarioDetailsService;

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    // Nota i18n: El cambio de idioma con ?lang= lo maneja el LocaleChangeInterceptor
    // definido en tu configuración i18n; aquí no se requiere nada especial.

    // Cadena 1: API - sin autenticación, sin formLogin, sin httpBasic
    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable()) // para pruebas; activar en prod según necesidad
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .logout(logout -> logout.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
        return http.build();
    }

    // Cadena 2: Web - con login y roles
    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Público: raíz y páginas básicas
                        .requestMatchers("/", "/login", "/registro", "/error", "/favicon.ico").permitAll()

                        // Recursos estáticos comunes: /css, /js, /images, /webjars, /favicon.ico
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()

                        // Rutas estáticas personalizadas del proyecto
                        .requestMatchers("/Imageneshome/**", "/ImagenesPerfil/**", "/h2-console/**").permitAll()

                        // Zonas con roles
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/arbitro/**").hasRole("ARBITRO")
                        .requestMatchers("/superAdmin/**").hasRole("SUPER_ADMIN")

                        // El resto autenticado
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureUrl("/login?error=true")
                        .successHandler(successHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable()) // para pruebas; activar en prod según necesidad
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}