package com.example.POMicroservice.APIValidation;

import com.example.POMicroservice.DTO.IsTokenInUseRequest;
import com.example.POMicroservice.DTO.RefreshTokenRequest;
import com.example.POMicroservice.DTO.RefreshTokenResponse;
import com.example.POMicroservice.DTO.ValidateSessionRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthApiService {

    // Instantiate webclient to point toward authentication service
    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080")
            .build();

    // Call authentication service to verify that token is in use
    public Mono<Boolean> isTokenInUse(IsTokenInUseRequest tokenInUseRequest) {
        return  webClient.post()
                .uri("/api/auth/isTokenInUse")
                .header("Content-Type", "application/json")
                .bodyValue(tokenInUseRequest)
                .retrieve()
                .bodyToMono(Boolean.class)
                ;
    }

    // Call authentication service to verify that user extracted from jwt token exists
    public Mono<Boolean> isSessionValid(ValidateSessionRequest validateSessionRequest) {
        return  webClient.post()
                .uri("/api/auth/validateSession")
                .header("Content-Type", "application/json")
                .bodyValue(validateSessionRequest)
                .retrieve()
                .bodyToMono(Boolean.class)
                ;
    }

    // Call to refresh token
    public Mono<RefreshTokenResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        return  webClient.post()
                .uri("/api/auth/refreshToken")
                .header("Content-Type", "application/json")
                .bodyValue(refreshTokenRequest)
                .retrieve()
                .bodyToMono(RefreshTokenResponse.class)
                ;
    }



}
