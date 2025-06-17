package com.example.POMicroservice.POMicroservice.APIValidation;

import com.example.POMicroservice.POMicroservice.POTempController;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;


@Service
@AllArgsConstructor
public class JwtService {

    private final POTempController poTempController;

    private final String secretKey = "37f097b03af124dc90ca968c2865a072a0a67fd98cc4d75a1e145889fc5cdbe2";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    public boolean isTokenValid(String token) {

        //1. Extract username in the token
        final String username = extractUsername(token);

        //2. Make an APICall to the authentication microservice to see if username is stored in database
        Mono<String> retrievedUserName = poTempController.callAuthenticationService();



        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}