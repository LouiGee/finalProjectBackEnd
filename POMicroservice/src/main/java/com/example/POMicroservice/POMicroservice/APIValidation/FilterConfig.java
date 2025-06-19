package com.example.POMicroservice.POMicroservice.APIValidation;

import jakarta.servlet.http.Cookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;


// This class runs before the SecurityConfig to validate the incoming request

@Component
public class FilterConfig extends OncePerRequestFilter {

    private final JwtService jwtService;

    public FilterConfig(JwtService jwtService) {
        this.jwtService = jwtService;
        ;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;

        // 1. Extract the jwt token from cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    System.out.println("Token found in request: " + token);
                }
            }
        }

        // Debug: System.out.println("Token valid: " + jwtService.isTokenValid(token));

        // 2.If the token is valid i.e the username in the token is present in the database and the token is within expiration
        if (token != null && jwtService.isTokenValid(token)) {

            String username = jwtService.extractUsername(token);

            // Create an authentication token that tells spring security(SecurityConfig) that the request is authenticated
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    username, null, Collections.emptyList()
            );

            // Set authentication token that will allow access to restricted endpoints
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // Allows request to continue through the chain of filters
        filterChain.doFilter(request, response);
    }
}