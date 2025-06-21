package com.example.POMicroservice.POMicroservice.APIValidation;

import com.example.POMicroservice.POMicroservice.DTO.GetUserRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthApiService {

    // Instantiate webclient to point toward authentication service
    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080")
            .build();

    // Call authentication service to verify that user extracted from jwt token exists
    public Mono<String> callAuthenticationService(GetUserRequest getUserRequest) {
        return  webClient.post()
                .uri("/api/auth/validateUserName")
                .header("Content-Type", "application/json")
                .bodyValue(getUserRequest)
                .retrieve()
                .bodyToMono(String.class)
                ;
    }

}
