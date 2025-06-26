package com.example.AuthMicroservice.AuthMicroservice.Services;

import com.example.AuthMicroservice.AuthMicroservice.Domain.Token;
import com.example.AuthMicroservice.AuthMicroservice.Repositories.TokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtService {

    //Secret key used to create a digital signature of jwt token
    private final String secretKey = "37f097b03af124dc90ca968c2865a072a0a67fd98cc4d75a1e145889fc5cdbe2";

    //Long dated refreshExpiration
    private final long refreshExpiration = 604800000;

    //Short dated refreshExpiration - should really be 15 minutes
    private final long jwtExpiration = 8640000;

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

    public long getRefreshExpiration() {
        return refreshExpiration;
    }

    public long getJwtExpiration() {
        return jwtExpiration;
    }
}