package com.example.AuthMicroservice.AuthMicroservice.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class AuthenticationResponse {

    private String token;
    private String refreshToken;
    private boolean mfaEnabled;
    private String secretImageUri;
}
