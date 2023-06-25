package com.bootcamp.rules_engine;

import com.bootcamp.rules_engine.enums.UserRole;
import com.bootcamp.rules_engine.model.Role;
import com.bootcamp.rules_engine.model.RulesEngineUser;
import com.bootcamp.rules_engine.repository.RoleRepository;
import com.bootcamp.rules_engine.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@SpringBootApplication
public class RulesEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(RulesEngineApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepository
			roleRepository, PasswordEncoder encoder) {
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
				.name("John")
				.lastName("Doe")
				.email("johndoe@email.com")
				.password(encoder.encode("password"))
				.role(adminRole)
				.build();
		RulesEngineUser consultantUser = RulesEngineUser.builder()
				.userId(UUID.randomUUID())
				.name("Jane")
				.lastName("Doe")
				.email("janedoe@email.com")
				.password(encoder.encode("password"))
				.role(consultantRole)
				.build();
		RulesEngineUser researcherUser = RulesEngineUser.builder()
				.userId(UUID.randomUUID())
				.name("Janice")
				.lastName("Doe")
				.email("janicedoe@email.com")
				.password(encoder.encode("password"))
				.role(researcherRole)
				.build();
		RulesEngineUser editorUser = RulesEngineUser.builder()
				.userId(UUID.randomUUID())
				.name("James")
				.lastName("Doe")
				.email("jamesdoe@email.com")
				.password(encoder.encode("password"))
				.role(editorRole)
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
