package com.atlasys.freeplanning.identity.controller;

import com.atlasys.freeplanning.identity.dto.AuthenticationRequest;
import com.atlasys.freeplanning.identity.dto.AuthenticationResponse;
import com.atlasys.freeplanning.identity.dto.RegisterRequest;
import com.atlasys.freeplanning.identity.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest data) {
        return ResponseEntity.ok(service.login(data));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest data) {
        return ResponseEntity.ok(service.register(data));
    }
}
