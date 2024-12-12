package com.application.security.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.application.security.Repositories.UtilisateurRepository;
import com.application.security.Services.UtilisateurService;

import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.GET;;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class ConfigurationSecurityApplication {
    private final UserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return  httpSecurity
                    .csrf(csrf -> csrf.disable()) // Désactiver CSRF si nécessaire
                    .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(POST,"/inscription").permitAll()
                        .requestMatchers(POST,"/activation").permitAll()
                        .requestMatchers(POST,"/connexion").permitAll()
                        .requestMatchers(POST,"/modifier-mot-de-passe").permitAll()
                        .requestMatchers(POST,"/nouveau-mot-de-passe").permitAll()
                        /* .requestMatchers(GET, "/avis").hasAnyRole("MANAGER","ADMINISTRATEUR") */
                        .requestMatchers(GET, "/utilisateur").hasAnyAuthority("ROLE_MANAGER","ROLE_ADMINISTRATEUR")
                        .anyRequest().authenticated()
                    )
                    
                    .sessionManagement(httpSecuritySessionManagementConfigurer -> 
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
     
                    .authenticationProvider(this.authenticationProvider())
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).build();
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(this.bCryptPasswordEncoder);
        return daoAuthenticationProvider;
    }
}
