package com.application.security.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.security.Entities.Validation;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


@Repository
public interface ValidationRepository extends JpaRepository<Validation,Integer>{
    Optional<Validation> findByCode(String code);
    void deleteAllByExpirationBefore(Instant now);
}
