package com.example.POMicroservice.DTO;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IsTokenInUseRequest {

    private String authenticationToken;
}
