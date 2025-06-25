package com.example.AuthMicroservice.AuthMicroservice.Services;

import com.example.AuthMicroservice.AuthMicroservice.DTO.AuthenticationRequest;
import com.example.AuthMicroservice.AuthMicroservice.DTO.AuthenticationResponse;
import com.example.AuthMicroservice.AuthMicroservice.DTO.RefreshTokenRequest;
import com.example.AuthMicroservice.AuthMicroservice.Domain.Permission;
import com.example.AuthMicroservice.AuthMicroservice.Domain.Session;
import com.example.AuthMicroservice.AuthMicroservice.Domain.Token;
import com.example.AuthMicroservice.AuthMicroservice.Domain.User;
import com.example.AuthMicroservice.AuthMicroservice.Repositories.TokenRepository;
import com.example.AuthMicroservice.AuthMicroservice.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import com.example.AuthMicroservice.AuthMicroservice.Repositories.SessionRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    // Inject beans to be used
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final TokenRepository tokenRepository;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        //1. Use request information to validate if user exists - if it does create an 'authenticationObject'
        var authenticationObject = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        //2. Create hashmap to eventually store the information about 'claims' - information about user
        var claims = new HashMap<String, Object>();

        //3. Extract the user from authenticationObject
        var user = ((User) authenticationObject.getPrincipal());

        //4. Insert email user email into claims
        claims.put("email", user.getUsername());

        //5. Generate sessionID and insert sessionID into claims
        sessionRepository.save(Session.builder().user(userRepository.returnUserByEmail(user.getUsername())).build());

        var sessionID = sessionRepository.findMostRecentSessionIDByUserID(userRepository.returnUserByEmail(user.getUsername()));

        claims.put("sessionID", sessionID);

        //6. Generate short dated Token
        var jwtToken = jwtService.generateToken(claims, user);

        //7. Generate long dated Token
        var refreshToken = jwtService.generateRefreshToken(user);

        //8. Save token to the database

        tokenRepository.save(Token.builder().authenticationToken(jwtToken)
                                            .refreshToken(refreshToken)
                                            .authenticationTokenExpiration(new Date(System.currentTimeMillis() + jwtService.getJwtExpiration()))
                                            .refreshTokenExpiration(new Date(System.currentTimeMillis() + jwtService.getRefreshExpiration()))
                                            .session(sessionRepository.findMostRecentSessionByUserID(userRepository.returnUserByEmail(user.getUsername())))
                                            .userEmail(user.getUsername())
                                            .userPermission(user.getAuthorities().iterator().next().getAuthority())
                                            .tokenInUse(true)
                                            .build());

        //8. Build Token
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();

    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshRequest) {

        return null;
    }


    public Boolean usernameFound (String email) {

        return userRepository.findByEmail(email).isPresent();

    }


}


