package com.example.AuthMicroservice;

import com.example.AuthMicroservice.AuthMicroservice.DTO.IsTokenInUseRequest;
import com.example.AuthMicroservice.AuthMicroservice.DTO.RefreshTokenRequest;
import com.example.AuthMicroservice.AuthMicroservice.DTO.ValidateSessionRequest;
import com.example.AuthMicroservice.AuthMicroservice.Services.AuthenticationService;
import com.example.AuthMicroservice.AuthMicroservice.DTO.AuthenticationRequest;
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