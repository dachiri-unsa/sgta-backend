package com.sgta.shared.config;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sgta.role.model.Role;
import com.sgta.role.model.RoleUser;
import com.sgta.role.repository.RoleRepository;
import com.sgta.shared.config.properties.AdminProperties;
import com.sgta.user.User;
import com.sgta.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminProperties adminProperties;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        crearAdminSiNoExiste();
    }

    private void crearAdminSiNoExiste() {
        if (userRepository.findByEmailAndStatusTrue(adminProperties.getEmail()).isPresent()) {
            log.info("Usuario admin ya existe, omitiendo creación.");
            return;
        }

        log.info("Creando usuario admin inicial...");

        Role adminRole = obtenerRol("ADMIN");

        User admin = construirAdmin(adminRole);

        userRepository.save(admin);

        log.info("Usuario admin creado exitosamente con email: {}", adminProperties.getEmail());
    }

    private User construirAdmin(Role adminRole) {
        User admin = new User();
        admin.setName("admin");
        admin.setEmail(adminProperties.getEmail());
        admin.setPassword(passwordEncoder.encode(adminProperties.getPassword()));
        admin.setStatus(true);
        admin.setRoleUsers(List.of(construirRoleUser(admin, adminRole)));
        return admin;
    }

    private RoleUser construirRoleUser(User user, Role role) {
        RoleUser roleUser = new RoleUser();
        roleUser.setUser(user);
        roleUser.setRole(role);
        roleUser.setStatus(true);
        return roleUser;
    }

    private Role obtenerRol(String nombreRol) {
        return roleRepository.findByName(nombreRol)
            .orElseThrow(() -> new IllegalStateException(
                "Rol '%s' no encontrado. Verifica que Flyway haya ejecutado el INSERT de roles.".formatted(nombreRol)
            ));
    }
}
