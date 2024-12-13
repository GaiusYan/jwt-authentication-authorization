package com.application.security.Security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.application.security.Entities.Jwt;
import com.application.security.Entities.Utilisateur;
import com.application.security.Services.UtilisateurService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter{

    private final UtilisateurService utilisateurService;
    private final JwtService jwtService;
    private final HandlerExceptionResolver handlerExceptionResolver;
    @Override
    protected void doFilterInternal(
        HttpServletRequest request, 
        HttpServletResponse response, 
        FilterChain filterChain)
            throws ServletException, IOException {
        String token = null; 
        String username = null;
        boolean isTokenExpired = true;
        Jwt jwt = null;
        final String authorization = request.getHeader("Authorization");
        try {
            
            if(authorization != null && authorization.startsWith("Bearer ")){
                token = authorization.substring(7);
                isTokenExpired = jwtService.isTokenExpired(token);
                username = jwtService.extractUsername(token);
                jwt = this.jwtService.tokenByValue(token);
        
            }
        
            if (!isTokenExpired && jwt.getUtilisateur().getUsername().equals(username) &&
                username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = utilisateurService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
           handlerExceptionResolver.resolveException(request, response, null, e);
        }
        
        filterChain.doFilter(request, response);
    }

}
