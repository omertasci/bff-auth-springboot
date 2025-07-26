package com.example.bffauth.controller;

import com.example.bffauth.model.AuthRequest;
import com.example.bffauth.model.AuthResult;
import com.example.bffauth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResult> authenticate(@Valid @RequestBody AuthRequest request) {
        AuthResult result = authService.authenticate(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(result);
    }
}
