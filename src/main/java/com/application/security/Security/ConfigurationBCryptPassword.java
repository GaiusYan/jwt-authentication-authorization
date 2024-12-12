package com.application.security.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ConfigurationBCryptPassword {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
