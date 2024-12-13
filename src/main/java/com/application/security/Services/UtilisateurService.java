package com.application.security.Services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.management.RuntimeMBeanException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.application.security.RoleType;
import com.application.security.Authentication.ResetPasswordRequest;
import com.application.security.Entities.Role;
import com.application.security.Entities.Utilisateur;
import com.application.security.Entities.Validation;
import com.application.security.Repositories.UtilisateurRepository;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/* 
 * exemple
 */
@Service
@RequiredArgsConstructor
public class UtilisateurService implements UserDetailsService{

    private final UtilisateurRepository utilisateurRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ValidationService validationService;

    public void inscription(Utilisateur utilisateur){

        if(!utilisateur.getEmail().contains("@")){
            throw new RuntimeException("Votre mail n'est pas valide");
        } 

        if(!utilisateur.getEmail().contains(".")){
            throw new RuntimeException("Votre mail n'est pas valide");
        } 

        

        Optional<Utilisateur> utilisateurOptional = this.utilisateurRepository.findByEmail(utilisateur.getEmail());
        if (utilisateurOptional.isPresent()) {
            throw new RuntimeException("Votre email est déjà utilisé");
        }

        utilisateur.setPassword(this.passwordEncoder.encode(utilisateur.getPassword()));
        Role role = new Role();
        role.setLibelle(RoleType.UTILISATEUR);
        if(utilisateur.getRole() != null && utilisateur.getRole().getLibelle().equals(RoleType.ADMINISTRATEUR)){
            role.setLibelle(RoleType.ADMINISTRATEUR);
            utilisateur.setActif(true);
        }
        utilisateur.setRole(role);
        
        if(role.getLibelle().equals(RoleType.UTILISATEUR))
            this.validationService.enregistrer(this.utilisateurRepository.save(utilisateur));
    }

    public Utilisateur getUtilisateurByUsername(String username){
        Optional<Utilisateur> utilisateurOptional = this.utilisateurRepository.findByEmail(username);
        if (!utilisateurOptional.isPresent()) {
            throw new RuntimeException("Cet utilisateur n'existe pas");
        }

        if (!utilisateurOptional.get().isActif()) {
            throw new RuntimeException("Le compte n'est pas actif");
        }
        return utilisateurOptional.get();
    }

    @Override
    public Utilisateur loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.utilisateurRepository
            .findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Cet utilisateur n'existe pas"));
    }

    public void modifierPassword(Map<String,String> param){
        Utilisateur utilisateur = loadUserByUsername(param.get("email"));
        this.validationService.enregistrer(utilisateur);
    }

    public void nouveauPassword(ResetPasswordRequest request){
        Utilisateur utilisateur = loadUserByUsername(request.getEmail());
        final Validation validation = validationService.getValidationByCode(request.getCode());
        if (validation.getUtilisateur().getEmail().equals(utilisateur.getEmail())) {
            String passwordEncrypt = this.passwordEncoder.encode(request.getPassword());
            utilisateur.setPassword(passwordEncrypt);
            this.utilisateurRepository.save(utilisateur);
        }
    }

    public List<Utilisateur> getUtilisateurs(){
        return this.utilisateurRepository.findAll();
    }
}
