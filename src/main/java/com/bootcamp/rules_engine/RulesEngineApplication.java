package com.bootcamp.rules_engine;


import com.bootcamp.rules_engine.enums.UserRole;
import com.bootcamp.rules_engine.model.Role;
import com.bootcamp.rules_engine.model.RulesEngineUser;
import com.bootcamp.rules_engine.repository.RoleRepository;
import com.bootcamp.rules_engine.repository.UserRepository;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.UUID;



@SpringBootApplication
@EnableSwagger2
public class RulesEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(RulesEngineApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
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

		return args -> {
			roleRepository.save(adminRole);
			roleRepository.save(consultantRole);
			roleRepository.save(researcherRole);
			userRepository.save(adminUser);
			userRepository.save(consultantUser);
			userRepository.save(researcherUser);
		};
	}
}

