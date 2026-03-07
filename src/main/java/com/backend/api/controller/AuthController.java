package com.backend.api.controller;

import com.backend.api.dto.requestDto.LoginRequestDto;
import com.backend.api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {

        authService.authenticate(
                request.getEmail(),
                request.getPassword()
        );

        return ResponseEntity.ok("Login Successful");
    }
}
