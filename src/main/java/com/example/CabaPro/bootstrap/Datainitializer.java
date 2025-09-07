package com.example.CabaPro.bootstrap;

import com.example.CabaPro.Services.AdministradorService;
import com.example.CabaPro.Services.ArbitroService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Datainitializer implements CommandLineRunner {

    private final AdministradorService administradorService;
    private final ArbitroService arbitroService;

    public Datainitializer(AdministradorService administradorService,
                           ArbitroService arbitroService) {
        this.administradorService = administradorService;
        this.arbitroService = arbitroService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // ADMINISTRADORES: username, email, nombre, apellido, rawPassword, tipo
        administradorService.createAdminIfNotExists("super", "super@caba.com", "Super", "Admin", "superpassword", "SUPER_ADMIN", "/ImagenesPerfil/SuperAdmin1.jpg");
        administradorService.createAdminIfNotExists("restrepoadmin", "restrepoadmin@caba.com", "Juan", "Restrepo", "adminpassword", "ADMIN", "/ImagenesPerfil/Admin1.jpg");

        // ARBITRO: username, email, nombre, apellido, rawPassword, especialidad, escalafon, disponibilidad
        arbitroService.createArbitroIfNotExists("Camiloref", "Camiloref@caba.com", "Camilo", "Perez",
                "refpassword", "Fútbol", "Categoria A", true,"/ImagenesPerfil/Arbitro1.jpg");
    }
}
