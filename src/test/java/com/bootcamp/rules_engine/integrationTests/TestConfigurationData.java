package com.bootcamp.rules_engine.integrationTests;

import com.bootcamp.rules_engine.enums.UserRole;
import com.bootcamp.rules_engine.model.Role;
import com.bootcamp.rules_engine.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.UUID;
@TestConfiguration
public class TestConfigurationData {
    @Bean
    CommandLineRunner commandLineRunner(RoleRepository roleRepository){
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

        return args -> {
            roleRepository.save(adminRole);
            roleRepository.save(consultantRole);
            roleRepository.save(researcherRole);
            roleRepository.save(editorRole);
        };
    }
}
