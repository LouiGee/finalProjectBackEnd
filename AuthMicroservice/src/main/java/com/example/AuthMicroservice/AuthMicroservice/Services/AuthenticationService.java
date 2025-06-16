
/*

package com.example.AuthMicroservice.AuthMicroservice.Services;

import com.example.AuthMicroservice.AuthMicroservice.AuthenticationRequest;
import com.example.AuthMicroservice.AuthMicroservice.DTO.AuthenticationResponse;
import com.example.AuthMicroservice.AuthMicroservice.Domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        //1. Use request information to validate if user exists - if it does create an authenticationObject
        var authenticationObject = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        //2. Create hashmap to eventually store the information about 'claims' - information about user
        var claims = new HashMap<String, Object>();

        //3. Extract the user
        var user = ((User) authenticationObject.getPrincipal());

        //4. Insert claims information into a hashmap (could theoretically be multiple claims)
        claims.put("fullName", user.getFullName());

        //5. Generate short dated Token
        var jwtToken = jwtService.generateToken(claims, user);

        //6. Generate long dated Token
        var refreshToken = jwtService.generateRefreshToken(user);

        //7. Build Token
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .mfaEnabled(false)
                .build();

    }

}



 */