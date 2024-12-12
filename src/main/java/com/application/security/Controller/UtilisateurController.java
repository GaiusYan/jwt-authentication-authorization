package com.application.security.Controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.security.Entities.Utilisateur;
import com.application.security.Services.UtilisateurService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("utilisateur")
@RestController
public class UtilisateurController {
    private final UtilisateurService utilisateurService;
    @PreAuthorize("hasAnyAuthority('ADMINISTRATEUR_READ','MANAGER_READ')")
    @GetMapping
    public List<Utilisateur> liste(){
        return utilisateurService.getUtilisateurs();
    }
}
