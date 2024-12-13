package com.application.security.Controller.Advice;

import java.nio.file.AccessDeniedException;
import java.security.SignatureException;
import java.util.Map;

import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.FORBIDDEN;;

@RestControllerAdvice
@Slf4j
public class ApplicationControllerAdvice {


    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(value = BadCredentialsException.class)
    public @ResponseBody ProblemDetail badCredentialException(BadCredentialsException exception){
        log.error(exception.getMessage(), exception);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(UNAUTHORIZED, "Identifiant invalide");
        problemDetail.setProperty("erreur", "Nous n'avons pas pu vous identifier");
        return problemDetail;
    }


    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(value = org.springframework.security.access.AccessDeniedException.class)
    public @ResponseBody ProblemDetail badCredentialException(AccessDeniedException exception){
        log.error(exception.getMessage(), exception);
        return ProblemDetail.forStatusAndDetail(
            FORBIDDEN, 
            "Vos droits ne vous permettent pas d'effectuer cette action");
    }
    
    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(value = {MalformedJwtException.class,SignatureException.class})
    public @ResponseBody ProblemDetail badCredentialException(final Exception exception){
        log.error(exception.getMessage(), exception);
        return ProblemDetail.forStatusAndDetail(UNAUTHORIZED, "Votre token est invalide");
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(value = Exception.class)
    public Map<String,String> exceptionHandler(){
        return Map.of("Erreur", "Description");
    }

}
