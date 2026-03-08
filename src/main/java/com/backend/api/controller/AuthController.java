package com.backend.api.controller;

import com.backend.api.dto.requestDto.LoginRequestDto;
import com.backend.api.dto.responseDto.LoginResponseDto;
import com.backend.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto request){

        return authService.authenticate(
                request.getEmail(),
                request.getPassword()
        );
    }
}