package com.example.POMicroservice.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenResponse {

    private String authenticationToken;
    private String refreshToken;

}
