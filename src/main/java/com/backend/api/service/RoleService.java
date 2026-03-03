package com.backend.api.service;

import com.backend.api.common.response.ApiResponse;
import com.backend.api.dto.requestDto.RoleRequestDto;
import com.backend.api.dto.responseDto.RoleResponseDto;
import com.backend.api.entity.Role;
import com.backend.api.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public ApiResponse createRole(RoleRequestDto request) {

        Role role = modelMapper.map(request, Role.class);

        Role savedRole = roleRepository.save(role);

        RoleResponseDto responseDto =
                modelMapper.map(savedRole, RoleResponseDto.class);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .message("Role created successfully")
                .data(Map.of("role", responseDto))
                .build();
    }

}
