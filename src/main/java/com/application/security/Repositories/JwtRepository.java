package com.application.security.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.security.Entities.Jwt;
import java.util.List;


@Repository
public interface JwtRepository extends JpaRepository<Jwt,Integer>{

    Optional<Jwt> findByValueAndDesactiveAndExpire(String value, boolean desactive, boolean expire);

}
