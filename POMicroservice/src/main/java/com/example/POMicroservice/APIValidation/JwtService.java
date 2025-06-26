package com.example.POMicroservice.APIValidation;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.POMicroservice.DTO.ValidateSessionRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;


@Service // Marks the class as a service bean
public class JwtService {

    // Used to inject the authApiService bean into the class
    private final AuthApiService authApiService;

    //Secret key used to verify the digital signature of jwt token
    private final String secretKey = "37f097b03af124dc90ca968c2865a072a0a67fd98cc4d75a1e145889fc5cdbe2";

    public JwtService(AuthApiService authApiService) {
        this.authApiService = authApiService;
    }

    //Used to extractSessionID from incoming jwt token
    public Long extractSessionID(String authenticationToken) {

        System.out.println("Passed authentication token: " + authenticationToken);

        DecodedJWT jwt = com.auth0.jwt.JWT.decode(authenticationToken);

        System.out.println("SessionID Extracted: " +jwt.getClaim("sessionID"));

        return jwt.getClaim("sessionID").asLong();
    }

    //Used to extractUsername from incoming jwt token
    public String extractUsername(String token) {

        DecodedJWT jwt = com.auth0.jwt.JWT.decode(token);

        return jwt.getSubject();
    }

    //Used to extract claims from the token e.g userName and Roles. Allows us to pass a function to access which part of the claim we want
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

        //1.Extract all claims
        final Claims claims = extractAllClaims(token);

        //2.Extract Username
        return claimsResolver.apply(claims);
    }

    // Helper method to extractClaim
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey()) // Verifies token using the secret key
                .build()
                .parseClaimsJws(token) // Parses and validates the token
                .getBody(); // Returns the claims (payload)
    }

    // Helper method to extractAllClaims
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes); //This converts your secretKey string (which is Base64-encoded) into a SecretKey objec
    }


    //Used to determine if the incoming token is valid
    public boolean isSessionIdInAuthenticationTokenValid(String authenticationToken) {

        //1. Extract username in the token
        Long sessionID = extractSessionID(authenticationToken);

        // Debug: System.out.println("extractedUsername: " + username);

        //2. Make an APICall to the authentication microservice to see if username is stored in database
        ValidateSessionRequest validateSessionRequest = new ValidateSessionRequest();
        validateSessionRequest.setSessionId(sessionID);

        Mono<Boolean> sessionFoundMono = authApiService.validateSession(validateSessionRequest);
        Boolean sessionFound = sessionFoundMono.block();

        // Debug: System.out.println("Username found: " + usernameFound);

        //3. Return true if provided username exists in the database and token hasnt expired
        return (Objects.equals(sessionFound, true)) && isAuthenticationTokenWithinExpiration(authenticationToken);

    }

    // Helper method to isTokenValid
    public boolean isAuthenticationTokenWithinExpiration(String authenticationToken) {
        return extractExpiration(authenticationToken).after(new Date());
    }

    // Helper method to isTokenExpired
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }



}