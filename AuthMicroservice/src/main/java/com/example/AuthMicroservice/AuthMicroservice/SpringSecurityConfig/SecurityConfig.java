package com.example.AuthMicroservice.AuthMicroservice.SpringSecurityConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration // marks the class a configuration class
@EnableWebSecurity // enables web security in application
@RequiredArgsConstructor // automatically creates a constructor
@EnableMethodSecurity(securedEnabled = true) //allows method level security annotations


public class SecurityConfig {

    // marks as a bean method
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(
                                        "/api/auth/**"
                                ).permitAll() //Specifically allows ANY client to access any endpoint that starts with /api/auth
                                .anyRequest()
                                .authenticated()
                );

        return http.build();
    }}
