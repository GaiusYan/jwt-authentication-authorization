package com.application.security.Controller;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.CollectionId;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.security.Authentication.AuthenticationRequest;
import com.application.security.Authentication.ResetPasswordRequest;
import com.application.security.Entities.Utilisateur;
import com.application.security.Security.JwtService;
import com.application.security.Services.UtilisateurService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class CompteController {
    private final UtilisateurService utilisateurService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    @PostMapping(path = "inscription")
    public void inscription(@RequestBody Utilisateur utilisateur){
        log.info("inscription");
        this.utilisateurService.inscription(utilisateur);
    }

    @PostMapping(path = "modifier-mot-de-passe")
    public void modifierPassword(@RequestBody Map<String,String> password){
        log.info("inscription");
        this.utilisateurService.modifierPassword(password);
    }

    @PostMapping(path = "nouveau-mot-de-passe")
    public void nouveauPassword(@RequestBody ResetPasswordRequest request){
        log.info("inscription");
        this.utilisateurService.nouveauPassword(request);
    }

    @PostMapping(path = "connexion")
    public Map<String,String> connexion(@RequestBody AuthenticationRequest authenticationRequest){
        Utilisateur utilisateur = this.utilisateurService.getUtilisateurByUsername(authenticationRequest.getUsername());
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(String.format("ROLE_%s", utilisateur.getRole().getLibelle())));
        final Authentication authentication =  this.authenticationManager.authenticate(
         new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword(),authorities)
        );
        if(authentication.isAuthenticated()){
            return this.jwtService.generateJwt(authenticationRequest.getUsername());
        } 
        return null;
    }
}
