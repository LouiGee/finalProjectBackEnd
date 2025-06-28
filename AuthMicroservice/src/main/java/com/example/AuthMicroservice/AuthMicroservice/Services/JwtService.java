package com.example.AuthMicroservice.AuthMicroservice.Services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtService {

    //Secret key used to create a digital signature of jwt token
    private final String secretKey = "37f097b03af124dc90ca968c2865a072a0a67fd98cc4d75a1e145889fc5cdbe2";

    //Long dated refreshExpiration
    private final long refreshExpiration = 604800000;

    //Short dated refreshExpiration - should really be 15 minutes
    private final long jwtExpiration = 600000;

    public String generateToken(
            Map<String, ?> extraClaims,
            String subject
    ) {
        return buildToken(extraClaims, subject, jwtExpiration);
    }

    public String generateRefreshToken(
            String subject
    ) {
        return buildToken(new HashMap<>(), subject, refreshExpiration);
    }

    private String buildToken(
            Map<String, ?> extraClaims,
            String subject,
            long expiration
    ) {

        if (!extraClaims.isEmpty()) {

            return Jwts
                    .builder()
                    .setClaims(extraClaims) // Set claims passed through
                    .setSubject(subject)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(getSignInKey()) // Digital signature
                    .compact();
        } else {

            return Jwts
                    .builder()
                    .setSubject(subject)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(getSignInKey()) // Digital signature
                    .compact();
        }
    }

    private Key getSignInKey() {

        // converts into secret key into a raw byte array that is an input to the hmac algorithm
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // Creates a key object suitable for HMAC-SHA signing algorithms
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //Used to extractUsername from incoming jwt token
    public String extractUsername(String authenticationToken) {

        return extractClaim(authenticationToken, Claims::getSubject);
    }


    //Used to extractSessionID from incoming jwt token
    public Long extractSessionID(String authenticationToken) {

        return extractClaim(authenticationToken, claims -> claims.get("sessionID", Long.class));
    }

    //Used to extractSessionID from incoming jwt token
    public String extractPermission(String authenticationToken) {

        return extractClaim(authenticationToken, claims -> claims.get("permission", String.class));
    }

    //Used to extract claims from the token e.g userName and Roles. Allows us to pass a function to access which part of the claim we want
    public <T> T extractClaim(String authenticationToken, Function<Claims, T> claimsResolver) {

        //1.Extract all claims
        final Claims claims = extractAllClaims(authenticationToken);

        //2.Extract Username
        return claimsResolver.apply(claims);
    }

    // Helper method to extractClaim
    private Claims extractAllClaims(String authenticationToken) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(authenticationToken)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            // Return the claims even though the token is expired
            return ex.getClaims();
        }
    }




    public long getRefreshExpiration() {
        return refreshExpiration;
    }

    public long getJwtExpiration() {
        return jwtExpiration;
    }
}