package com.application.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.application.security.Entities.Role;
import com.application.security.Entities.Utilisateur;
import com.application.security.Repositories.UtilisateurRepository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@SpringBootApplication
@EnableScheduling
public class SecurityApplication{
	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}
}
