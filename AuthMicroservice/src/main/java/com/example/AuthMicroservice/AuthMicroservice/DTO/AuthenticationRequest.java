package com.example.AuthMicroservice.AuthMicroservice.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)

public class AuthenticationRequest {

    private String email;
    private String password;

}
