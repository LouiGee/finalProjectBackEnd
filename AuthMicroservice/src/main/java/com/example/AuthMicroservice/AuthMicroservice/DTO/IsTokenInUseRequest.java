package com.example.AuthMicroservice.AuthMicroservice.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IsTokenInUseRequest {

    private String authenticationToken;
}
