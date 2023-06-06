package com.project.wine_store.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class WebSecurityConfiguration {

    private JWTRequestFilter jwtRequestFilter;

    public WebSecurityConfiguration(JWTRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable().cors().disable();
        httpSecurity.addFilterBefore(jwtRequestFilter, AuthorizationFilter.class);
        httpSecurity.authorizeHttpRequests()
                // oricine poate sa vada vinurile (cu sau fara autentificare)
                .requestMatchers("/wine").permitAll()
                // oricine poate sa se inregistreze in aplicatie
                .requestMatchers("/auth/register").permitAll()
                // oricine poate sa se logheze in aplicatie
                .requestMatchers("/auth/login").permitAll()
                // oricine poate sa isi activeze contul
                .requestMatchers("/auth/verify").permitAll()
                .requestMatchers("/error").permitAll()
                // oricine poate sa ceara sa isi reinnoiasca parola
                .requestMatchers("/auth/forgotPassword").permitAll()
                // oricine poate sa isi reseteze parola
                .requestMatchers("/auth/resetPassword").permitAll()
                // orice alt request are nevoie de autentificare
                .anyRequest().authenticated();
        return httpSecurity.build();
    }

}
