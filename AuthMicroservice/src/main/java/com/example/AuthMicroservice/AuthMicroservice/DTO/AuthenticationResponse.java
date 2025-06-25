package com.example.AuthMicroservice.AuthMicroservice.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse {


    private String token;
    private String refreshToken;

}
