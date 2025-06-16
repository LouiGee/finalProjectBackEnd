package com.example.AuthMicroservice.AuthMicroservice.Repositories;

import com.example.testing22092024.Domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    Optional<Token> findByToken(String token);
}
