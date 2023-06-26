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


}
