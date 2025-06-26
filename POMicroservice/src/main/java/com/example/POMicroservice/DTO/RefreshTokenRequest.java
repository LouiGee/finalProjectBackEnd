package com.example.POMicroservice.DTO;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class RefreshTokenRequest {

    private final String refreshToken;
    private final String authenticationToken;

}
