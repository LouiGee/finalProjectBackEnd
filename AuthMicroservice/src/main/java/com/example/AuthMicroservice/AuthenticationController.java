package com.example.AuthMicroservice;

import com.example.AuthMicroservice.AuthMicroservice.DTO.IsTokenInUseRequest;
import com.example.AuthMicroservice.AuthMicroservice.DTO.RefreshTokenRequest;
import com.example.AuthMicroservice.AuthMicroservice.DTO.ValidateSessionRequest;
import com.example.AuthMicroservice.AuthMicroservice.Services.AuthenticationService;
import com.example.AuthMicroservice.AuthMicroservice.DTO.AuthenticationRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {

        return ResponseEntity.ok(authenticationService.authenticate(request));

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(name = "authenticationToken", required = false) String sessionCookie, HttpServletResponse response) {

        System.out.println(sessionCookie);

        String logoutMessage = authenticationService.logout(sessionCookie);

        // Clear the "authenticationToken" cookie
        Cookie authenticationTokenCookie = new Cookie("authenticationToken", null);
        authenticationTokenCookie.setHttpOnly(true);
        // cookie.setSecure(true); // set to true in production
        authenticationTokenCookie.setPath("/");
        authenticationTokenCookie.setMaxAge(0); // delete immediately
        response.addCookie(authenticationTokenCookie);

        // Clear the "authenticationToken" cookie
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        // cookie.setSecure(true); // set to true in production
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0); // delete immediately
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(logoutMessage);

    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {

        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }


    @PostMapping("/validateSession")
    public ResponseEntity<?> validateSession(@RequestBody ValidateSessionRequest sessionRequest){
        return ResponseEntity.ok(authenticationService.sessionFound(sessionRequest.getSessionId()));

    }

    @PostMapping("/isTokenInUse")
    public ResponseEntity<?> isTokenInUse(@RequestBody IsTokenInUseRequest request){
        return ResponseEntity.ok(authenticationService.isTokenInUse(request.getAuthenticationToken()));

    }



}