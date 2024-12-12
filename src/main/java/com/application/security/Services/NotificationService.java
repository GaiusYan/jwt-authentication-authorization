package com.application.security.Services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.application.security.Entities.Validation;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender javaMailSender;
    public void envoyer(Validation validation){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@gaiusyan");
        message.setTo(validation.getUtilisateur().getEmail());
        message.setSubject("Votre code d'activation");

        String text = String.format("Bonjour %s </br> Votre code d'activation est %s",
         validation.getUtilisateur().getNom(),
         validation.getCode());
         
        message.setText(text);
        javaMailSender.send(message);
    }
}
