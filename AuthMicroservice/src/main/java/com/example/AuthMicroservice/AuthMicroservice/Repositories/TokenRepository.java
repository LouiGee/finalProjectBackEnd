package com.example.AuthMicroservice.AuthMicroservice.Repositories;

import com.example.AuthMicroservice.AuthMicroservice.Domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByAuthenticationToken(String authenticationToken);

}