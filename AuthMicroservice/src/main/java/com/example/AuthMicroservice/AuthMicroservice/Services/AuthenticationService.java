package com.example.AuthMicroservice.AuthMicroservice.Services;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.AuthMicroservice.AuthMicroservice.DTO.AuthenticationRequest;
import com.example.AuthMicroservice.AuthMicroservice.DTO.AuthenticationResponse;
import com.example.AuthMicroservice.AuthMicroservice.DTO.RefreshTokenRequest;
import com.example.AuthMicroservice.AuthMicroservice.Domain.Session;
import com.example.AuthMicroservice.AuthMicroservice.Domain.JWTToken;
import com.example.AuthMicroservice.AuthMicroservice.Domain.User;
import com.example.AuthMicroservice.AuthMicroservice.Repositories.JWTTokenRepository;
import com.example.AuthMicroservice.AuthMicroservice.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import com.example.AuthMicroservice.AuthMicroservice.Repositories.SessionRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    // Inject beans to be used
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final JWTTokenRepository jwtTokenRepository;

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

        //4. Fetch authorities using userDetails class - its a list as there may be several authorities
        var authorities = user.getAuthorities()
                .stream().
                map(GrantedAuthority::getAuthority).
                toList();

        //5. Add permissions to the claims
        claims.put("permission", authorities.getFirst());

        //6. Generate sessionID and insert sessionID into claims
        sessionRepository.save(Session.builder().user(userRepository.returnUserByEmail(user.getUsername())).build());

        Long sessionID = sessionRepository.findTopByUserOrderByDateTimeStartedDesc(userRepository.returnUserByEmail(user.getUsername())).getSessionid();

        claims.put("sessionID", sessionID);

        //7. Generate short dated Token
        var authenticationToken = jwtService.generateToken(claims, user.getEmail());

        //8. Generate long dated Token
        var refreshToken = jwtService.generateRefreshToken(user.getEmail());

        //9. Save token to the database
        jwtTokenRepository.save(JWTToken.builder().authenticationToken(authenticationToken)
                                            .refreshToken(refreshToken)
                                            .authenticationTokenExpiration(new Date(System.currentTimeMillis() + jwtService.getJwtExpiration()))
                                            .refreshTokenExpiration(new Date(System.currentTimeMillis() + jwtService.getRefreshExpiration()))
                                            .session(sessionRepository.findTopByUserOrderByDateTimeStartedDesc(userRepository.returnUserByEmail(user.getUsername())))
                                            .userEmail(user.getUsername())
                                            .userPermission(user.getAuthorities().iterator().next().getAuthority())
                                            .tokenInUse(true)
                                            .build());

        //10. Build Token
        return AuthenticationResponse.builder()
                .authenticationToken(authenticationToken)
                .refreshToken(refreshToken)
                .build();

    }

    public String logout(String authenticationTokenString) {

        //1. Add an end date to the session
        Long sessionID = jwtService.extractSessionID(authenticationTokenString);

        //2. Find the session with that session ID
        Session session = sessionRepository.findSessionBySessionID(sessionID);

        //3. Set the finish time to mark the session as logged out
        session.setDateTimeFinish(LocalDateTime.now());

        //4. Save the updated session
        sessionRepository.save(session);

        //6. Invalidate jwt token
        Optional<JWTToken> jwtToken = jwtTokenRepository.findByAuthenticationToken(authenticationTokenString);

        if (jwtToken.isPresent()) {
            JWTToken token = jwtToken.get();
            token.setTokenInUse(false); // update field
            jwtTokenRepository.save(token); // save updated entity
        } else {
            throw new RuntimeException("JWTToken not found associated with " + authenticationTokenString );
        }

        //7. Wipe cookies from browser


        return "Logged out successfully";


    }



    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshRequest) {

        //1. Extract claims

        var extractedClaims = new HashMap<String,Object>();


        extractedClaims.put("permission", jwtService.extractPermission(refreshRequest.getAuthenticationToken()));
        extractedClaims.put("sessionID", jwtService.extractSessionID(refreshRequest.getAuthenticationToken()));
        String subject = jwtService.extractUsername(refreshRequest.getAuthenticationToken());

        //2. Generate new authentication Token

        var authenticationToken = jwtService.generateToken(extractedClaims, subject);

        //3. Generate new refresh Token

        var refreshToken = jwtService.generateRefreshToken(subject);

        //4. Save new token

        jwtTokenRepository.save(JWTToken.builder().authenticationToken(authenticationToken)
                .refreshToken(refreshToken)
                .authenticationTokenExpiration(new Date(System.currentTimeMillis() + jwtService.getJwtExpiration()))
                .refreshTokenExpiration(new Date(System.currentTimeMillis() + jwtService.getRefreshExpiration()))
                .session(sessionRepository.findTopByUserOrderByDateTimeStartedDesc(userRepository.returnUserByEmail(subject)))
                .userEmail(subject)
                .userPermission((String) extractedClaims.get("permission"))
                .tokenInUse(true)
                .build());

        //5. Update previous token to not inuse

        Optional<JWTToken> previousToken = jwtTokenRepository.findByAuthenticationToken(refreshRequest.getAuthenticationToken());

        if (previousToken.isPresent()) {
            JWTToken token = previousToken.get();
            token.setTokenInUse(false); // update field
            jwtTokenRepository.save(token); // save updated entity
        } else {
            throw new RuntimeException("Token not found with authenticationToken: " + refreshRequest.getAuthenticationToken());
        }

        //6. Return authentication response

        return AuthenticationResponse.builder().authenticationToken(authenticationToken).refreshToken(refreshToken).build();
    }


    public Boolean sessionFound (Long sessionID) {

        return sessionRepository.findById(sessionID).isPresent();

    }

    public Boolean isTokenInUse (String authenticationToken) {

        return jwtTokenRepository.findByAuthenticationToken(authenticationToken).get().getTokenInUse();

    }


}


