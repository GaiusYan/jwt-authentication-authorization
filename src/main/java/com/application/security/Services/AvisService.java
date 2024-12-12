package com.application.security.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.application.security.Entities.Avis;
import com.application.security.Repositories.AvisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvisService {
    private final AvisRepository avisRepository;
    
    public void creer(Avis avis){
        this.avisRepository.save(avis);
    }

    public List<Avis> liste(){
        return this.avisRepository.findAll();
    }
}
