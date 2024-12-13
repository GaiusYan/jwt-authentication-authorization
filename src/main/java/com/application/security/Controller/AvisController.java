package com.application.security.Controller;

import java.util.List;

import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.application.security.Entities.Avis;
import com.application.security.Entities.Utilisateur;
import com.application.security.Services.AvisService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("avis")
@RequiredArgsConstructor
public class AvisController {
    private final AvisService avisService;

    @PostMapping
    public void creer(@RequestBody Avis avis){
        Utilisateur utilisateur = (Utilisateur)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        avis.setUtilisateur(utilisateur);
        this.avisService.creer(avis);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATEUR')")
    @GetMapping
    public List<Avis> liste(){
       // Utilisateur utilisateur = (Utilisateur)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.avisService.liste();
    }
}
