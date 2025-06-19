package com.example.AuthMicroservice.AuthMicroservice.Services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService {

    //Secret key used to create a digital signature of jwt token
    private final String secretKey = "37f097b03af124dc90ca968c2865a072a0a67fd98cc4d75a1e145889fc5cdbe2";

    //Long dated refreshExpiration
    private final long refreshExpiration = 604800000;

    //Short dated refreshExpiration - should really be 15 minutes
    private final long jwtExpiration = 8640000;


    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {

        // Fetch authorities using userDetails class - its a list as there may be several authorities
        var authorities = userDetails.getAuthorities()
                .stream().
                map(GrantedAuthority::getAuthority).
                toList();

        return Jwts
                .builder()
                .setClaims(extraClaims) // Set claims passed through
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .claim("authorities", authorities) //Set authority claim i.e extra info about the user
                .signWith(getSignInKey()) // Digital signature
                .compact();
    }

    private Key getSignInKey() {

        // Decoders coverts that Base64 string into a byte array byte[]
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // Creates a key object suitable for HMAC-SHA signing algorithms
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateRefreshToken(
            UserDetails userDetails
    ) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

}