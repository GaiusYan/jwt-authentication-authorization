package com.application.security.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.application.security.Entities.Utilisateur;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur,Integer>{

    Optional<Utilisateur> findByEmail(String email);
    Optional<Utilisateur> findByEmailAndPassword(String email,String password);
}
