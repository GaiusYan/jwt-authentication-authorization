package com.application.security.Security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.hibernate.boot.jaxb.mapping.DiscriminatedAssociation.Key;
import org.springframework.stereotype.Service;

import com.application.security.Authentication.AuthenticationRequest;
import com.application.security.Entities.Jwt;
import com.application.security.Entities.Utilisateur;
import com.application.security.Repositories.JwtRepository;
import com.application.security.Services.UtilisateurService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final UtilisateurService utilisateurService;
    private final JwtRepository jwtRepository;
    private final String ENCRYPTION_KEY = "dd5e9770f0a84810f98cdf97b96c99fc9c928643b3a46394ce0a02ef6dd7df9f";
    private final String BEARER = "bearer";

    public Map<String,String> generateJwt(String username){
        Utilisateur utilisateur = this.utilisateurService.loadUserByUsername(username);
        final Map<String,String> jwtMap = this.generateJwt(utilisateur);
        final Jwt jwt = Jwt.builder()
            .desactive(false)
            .expire(false)
            .utilisateur(utilisateur)
            .value(jwtMap.get(BEARER))
            .build();
            jwtRepository.save(jwt);
        return jwtMap;
    }

    public Jwt tokenByValue(String value){
       return this.jwtRepository.findByValueAndDesactiveAndExpire(
            value,
            false,
            false
        )
        .orElseThrow(() -> new RuntimeException("Token n'est pas valide"));
    }

    private Map<String,String> generateJwt(Utilisateur utilisateur){
        final long currentTime = System.currentTimeMillis();
        final long currentTimeExpiration = currentTime + 30 * 60 * 1000;
        Map<String,Object> claims = new HashMap<>() ;
        claims.put("nom", utilisateur.getNom());
        claims.put("email", utilisateur.getEmail());
        claims.put(Claims.EXPIRATION, currentTimeExpiration);
        claims.put(Claims.SUBJECT, utilisateur.getEmail());
        final String bearer = Jwts.builder()
            .setIssuedAt(new Date(currentTime))
            .setExpiration(new Date(currentTimeExpiration))
            .setSubject(utilisateur.getEmail())
            .setClaims(claims)
            .signWith(getKey(), SignatureAlgorithm.HS256)
            .compact();
        return Map.of(
            BEARER, bearer
        );
    }

    private java.security.Key getKey(){
        final byte[] decoders = Decoders.BASE64.decode(ENCRYPTION_KEY);
        return Keys.hmacShaKeyFor(decoders);
    }

    public String extractUsername(String token){
        return this.getClaim(token,Claims::getSubject);
    }

    public boolean isTokenExpired(String token){
        Date expirationDate = this.getClaim(token,Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    /* private Date getExpirationDateFromToken(String token){
        return this.getClaim(token,Claims::getExpiration);
    } */

    private <T> T getClaim(String token, Function<Claims,T> function){
        Claims claims = getAllClaims(token);
        return function.apply(claims);
    }

    private Claims getAllClaims(String token){
        return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
    }
}
