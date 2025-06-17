package com.example.AuthMicroservice;

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
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {

        return ResponseEntity.ok(authenticationService.authenticate(request));

    }

    @GetMapping("/getUserName")
    public ResponseEntity<?> getUserName(String username){

        return ResponseEntity.ok(authenticationService.getUserName(username));

    }



}