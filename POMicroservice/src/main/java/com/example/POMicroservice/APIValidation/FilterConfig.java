package com.example.POMicroservice.APIValidation;

import com.example.POMicroservice.DTO.IsTokenInUseRequest;
import com.example.POMicroservice.DTO.RefreshTokenRequest;
import com.example.POMicroservice.DTO.RefreshTokenResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Collections;


// This class runs before the SecurityConfig to validate the incoming request

@Component
@RequiredArgsConstructor
public class FilterConfig extends OncePerRequestFilter {

    // Used to inject the service classes into the class
    private final JwtService jwtService;

    private final AuthApiService authApiService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authenticationToken = null;
        String refreshToken = null;

        // 1. Extract the jwt token from cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("authenticationToken".equals(cookie.getName())) {
                    authenticationToken = cookie.getValue();
                }
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        // Debug: System.out.println("Token found in request: " + authenticationToken);

        // Debug: System.out.println("Token valid: " + jwtService.isTokenValid(token));

        // 1. Is authenticationToken present

        boolean authenticationTokenPresent = false;

        if (authenticationToken != null) {
            authenticationTokenPresent = true;
        }

        // 2. Is the authenticationToken still in use

        boolean authenticationTokenStillInUse = false;

        authenticationTokenStillInUse = Boolean.TRUE.equals(authApiService.isTokenInUse(IsTokenInUseRequest.builder().authenticationToken(authenticationToken).build()).block());

        // 3. Is the authenticationToken expiry date is within its expiry date - this step uses the signature to verify the expiration claim

        boolean authenticationTokenWithinExpiry = false;

        if (authenticationTokenPresent && jwtService.isAuthenticationTokenWithinExpiration(authenticationToken)) {
            authenticationTokenWithinExpiry = true;
        }

         System.out.println("tokenPresent:" + authenticationTokenPresent + " tokenWithinExpiry:" + authenticationTokenWithinExpiry + "tokenStillinUse" + authenticationTokenStillInUse);

        // 4. If authenticationToken is present, is still in use but has expired then request token refresh

        if (authenticationTokenPresent && authenticationTokenStillInUse && !authenticationTokenWithinExpiry) {

            Mono<RefreshTokenResponse> refreshTokenResponse = authApiService.refreshToken(RefreshTokenRequest.builder()
                    .authenticationToken(authenticationToken)
                    .refreshToken(refreshToken)
                    .build());

            RefreshTokenResponse refreshResponse = refreshTokenResponse.block();

            assert refreshResponse != null;

            // Add valid tokens to current HTTP request


            // Add refreshed tokens to response cookie for subsequent calls


            Cookie authenticationCookie = new Cookie("authenticationToken", refreshResponse.getAuthenticationToken());
            authenticationCookie.setPath("/");

            Cookie refreshCookie = (new Cookie("refreshToken", refreshResponse.getRefreshToken()));
            refreshCookie.setPath("/");

            response.addCookie(authenticationCookie);
            response.addCookie(refreshCookie);

            // update local authentication and refresh tokens

            authenticationToken = refreshResponse.getAuthenticationToken();
            refreshToken = refreshResponse.getRefreshToken();

            // if refreshed tokens are in the HTTP response, the HTTP request can now be deemed to be good

            authenticationTokenWithinExpiry = true;

        }

        // 4 .Is there a valid session associated with the token, if so create an authtoken to validate entry into API

        if (authenticationTokenPresent && authenticationTokenWithinExpiry && jwtService.isSessionIdInAuthenticationTokenValid(authenticationToken)) {

            // Extract username
            String username = jwtService.extractUsername(authenticationToken);

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