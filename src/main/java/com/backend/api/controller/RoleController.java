package com.backend.api.controller;

import com.backend.api.common.response.ApiResponse;
import com.backend.api.dto.requestDto.RoleRequestDto;
import com.backend.api.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/roles")
    public ResponseEntity<ApiResponse> createRole(
            @RequestBody RoleRequestDto request
            ){
        ApiResponse response = roleService.createRole(request);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/roles")
    public ResponseEntity<ApiResponse> getAllRoles() {

        ApiResponse response = roleService.getAllRole();

        return ResponseEntity
                .status(response.getCode())
                .body(response);
    }

}
