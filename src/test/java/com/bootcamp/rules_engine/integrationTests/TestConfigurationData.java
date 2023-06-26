package com.bootcamp.rules_engine.integrationTests;

import com.bootcamp.rules_engine.enums.UserRole;
import com.bootcamp.rules_engine.model.Role;
import com.bootcamp.rules_engine.model.RulesEngineUser;
import com.bootcamp.rules_engine.repository.RoleRepository;
import com.bootcamp.rules_engine.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;
@TestConfiguration
public class TestConfigurationData {
    @Bean
    CommandLineRunner commandLineRunner(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder){
        Role adminRole = Role.builder()
                .roleId(UUID.randomUUID())
                .roleName(UserRole.ADMIN.getRole())
                .description("Administrador: usuario que crea las columnas de las tablas y asigna roles a usuarios ya registrados.")
                .build();

        Role consultantRole = Role.builder()
                .roleId(UUID.randomUUID())
                .roleName(UserRole.CONSULTANT.getRole())
                .description("Consultor: usuario que valida un registro en específico contra una regla ya creada.")
                .build();

        Role researcherRole = Role.builder()
                .roleId(UUID.randomUUID())
                .roleName(UserRole.RESEARCHER.getRole())
                .description("Investigador: usuario que solo crea reglas.")
                .build();

        Role editorRole = Role.builder()
                .roleId(UUID.randomUUID())
                .roleName(UserRole.EDITOR.getRole())
                .description("Editor: usuario que solo ingresa registros (llena filas de las tablas).")
                .build();

        RulesEngineUser adminUser = RulesEngineUser.builder()
                .userId(UUID.randomUUID())
                .name("Laura Daniela")
                .lastName("Martinez Ortiz")
                .email("lauramartinez@gmail.com")
                .password(passwordEncoder.encode("password"))
                .role(adminRole)
                .build();

        RulesEngineUser consultantUser = RulesEngineUser.builder()
                .userId(UUID.randomUUID())
                .name("Keren López")
                .lastName("Córdoba")
                .email("kerenlopez@gmail.com")
                .password(passwordEncoder.encode("password"))
                .role(consultantRole)
                .build();

        RulesEngineUser researcherUser = RulesEngineUser.builder()
                .userId(UUID.randomUUID())
                .name("Samuel")
                .lastName("Arias Delgado")
                .email("samuel@gmail.com")
                .password(passwordEncoder.encode("password"))
                .role(researcherRole)
                .build();

        RulesEngineUser editorUser = RulesEngineUser.builder()
                .userId(UUID.randomUUID())
                .name("Sara")
                .lastName("Alvarez Ordonez")
                .email("sara@gmail.com")
                .password(passwordEncoder.encode("password"))
                .role(researcherRole)
                .build();

        return args -> {
            roleRepository.save(adminRole);
            roleRepository.save(consultantRole);
            roleRepository.save(researcherRole);
            roleRepository.save(editorRole);
            userRepository.save(adminUser);
            userRepository.save(consultantUser);
            userRepository.save(researcherUser);
            userRepository.save(editorUser);
        };
    }
}
