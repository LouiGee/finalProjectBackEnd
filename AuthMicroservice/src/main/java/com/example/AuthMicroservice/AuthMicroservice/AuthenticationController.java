package com.example.AuthMicroservice.AuthMicroservice;

import com.example.AuthMicroservice.AuthMicroservice.Services.JwtService;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final JwtService JwtService;


    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {

        return ResponseEntity.ok(JwtService.generateToken(request.getEmail()));

    }


}