package com.application.security.Services;

import java.time.Instant;
import java.util.Random;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.application.security.Entities.Utilisateur;
import com.application.security.Entities.Validation;
import com.application.security.Repositories.ValidationRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.time.temporal.ChronoUnit.MINUTES;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationService {
    private final ValidationRepository validationRepository;
    private final NotificationService notificationService;

    public void enregistrer(Utilisateur utilisateur){
        Validation validation = new Validation();
        validation.setUtilisateur(utilisateur);
        Instant creation = Instant.now();
        validation.setCreation(creation);
        Instant expiration = Instant.now();
        expiration = creation.plus(10, MINUTES);
        Random random = new Random();
        Integer randomInteger = random.nextInt(999999);
        String code = String.format("%06d", randomInteger);

        validation.setCode(code);
        validation.setExpiration(expiration);
        this.validationRepository.save(validation);
        this.notificationService.envoyer(validation);
    }

    public Validation getValidationByCode(String code){
        return this.validationRepository.findByCode(code)
                    .orElseThrow(() -> new RuntimeException("Ce code de validation n'existe pas"));
    }

    @Scheduled(cron = "*/30 * * * * *")
    public void cleanTable(){
        log.info("suppression des tokens à {}",Instant.now());
        this.validationRepository.deleteAllByExpirationBefore(Instant.now());
    }
}
