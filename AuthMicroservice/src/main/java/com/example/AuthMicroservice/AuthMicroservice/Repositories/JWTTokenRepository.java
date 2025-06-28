package com.example.AuthMicroservice.AuthMicroservice.Repositories;

import com.example.AuthMicroservice.AuthMicroservice.Domain.JWTToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JWTTokenRepository extends JpaRepository<JWTToken, Long> {

    Optional<JWTToken> findByAuthenticationToken(String authenticationToken);

}