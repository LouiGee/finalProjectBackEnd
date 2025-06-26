package com.example.AuthMicroservice.AuthMicroservice.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest {

    private String authenticationToken;
    private String refreshToken;

}

