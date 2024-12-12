package com.application.security.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.application.security.Entities.Avis;

@Repository
public interface AvisRepository extends JpaRepository<Avis,Integer>{

}
