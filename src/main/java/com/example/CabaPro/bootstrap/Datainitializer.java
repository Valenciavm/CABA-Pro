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
                "refpassword", "Municipal", true,"https://cabapro.s3.us-east-1.amazonaws.com/arbitro1.jpg");
        arbitroService.createArbitroIfNotExists("arbitro2", "LuisaRef@caba.com", "Luisa", "Fernandez",
                "abc123", "Internacional", true,"https://cabapro.s3.us-east-1.amazonaws.com/arbitro2.jpg");
        arbitroService.createArbitroIfNotExists("arbitro3", "CarlosRef@caba.com", "Carlos", "Gomez",
                "def456", "Nacional", true,"https://cabapro.s3.us-east-1.amazonaws.com/arbitro3.jpg");
        arbitroService.createArbitroIfNotExists("anaarbitro4", "AnaRef@caba.com", "Ana", "Gomez",
                "def456", "Nacional", true,"https://cabapro.s3.us-east-1.amazonaws.com/arbitro4.jpg");
        arbitroService.createArbitroIfNotExists("josearbitro5", "JoseRef@caba.com", "Jose", "Gomez",
                "def456", "Nacional", true,"https://cabapro.s3.us-east-1.amazonaws.com/arbitro5.jpg");
        arbitroService.createArbitroIfNotExists("juanarbitro", "JuanRef@caba.com", "Juan", "Gomez",
                "def456", "Nacional", true,"https://cabapro.s3.us-east-1.amazonaws.com/arbitro6.jpg");
        arbitroService.createArbitroIfNotExists("sararbitro", "JuanRef@caba.com", "Sara", "Gomez",
                "def456", "Nacional", true,"https://cabapro.s3.us-east-1.amazonaws.com/arbitro7.jpg");
                
    }
}
