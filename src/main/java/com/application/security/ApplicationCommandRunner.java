/* package com.application.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.application.security.Entities.Role;
import com.application.security.Entities.Utilisateur;
import com.application.security.Repositories.UtilisateurRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationCommandRunner {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Bean
    public CommandLineRunner commandLineRunner(UtilisateurRepository utilisateurRepository){
        return args -> {

            Utilisateur manager = Utilisateur.builder()
                            .actif(true)
                            .email("manager@gmail.com")
                            .nom("manager")
                            .password(bCryptPasswordEncoder.encode("manager"))
                            .role(Role.builder().libelle(RoleType.MANAGER).build())
                            .build();
            utilisateurRepository.save(manager);
        };
    }
}
 */