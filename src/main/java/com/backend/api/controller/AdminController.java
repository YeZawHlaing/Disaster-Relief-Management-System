package com.backend.api.controller;

import com.backend.api.common.response.ApiResponse;
import com.backend.api.common.response.ResponseUtils;
import com.backend.api.dto.requestDto.UserRequestDto;
import com.backend.api.dto.responseDto.UserResponseDto;
import com.backend.api.entity.User;
import com.backend.api.service.RoleService;
import com.backend.api.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminController {

    private final UserService userService;

    @PostMapping("/admin/register")
    public ResponseEntity<ApiResponse> createUser(
            @RequestBody final UserRequestDto userRequest,
            final HttpServletRequest request
    ) {
        final ApiResponse response = this.userService.createUser(userRequest);
        return ResponseUtils.buildResponse(request, response);
    }



}
