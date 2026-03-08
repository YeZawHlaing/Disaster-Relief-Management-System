package com.backend.api.service;

import com.backend.api.dto.responseDto.LoginResponseDto;
import com.backend.api.entity.User;
import com.backend.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public LoginResponseDto authenticate(String email, String password) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LoginResponseDto response = new LoginResponseDto();
        response.setId(user.getId());
        response.setEmail(user.getEmail());

        return response;
    }
}