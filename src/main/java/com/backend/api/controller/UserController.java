package com.backend.api.controller;

import com.backend.api.common.response.ApiResponse;
import com.backend.api.common.response.ResponseUtils;
import com.backend.api.dto.requestDto.UserRequestDto;
import com.backend.api.dto.responseDto.UserResponseDto;
import com.backend.api.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<ApiResponse> createUser(
            @RequestBody final UserRequestDto userRequest,
            final HttpServletRequest request
    ) {
        final ApiResponse response = this.userService.createUser(userRequest);
        return ResponseUtils.buildResponse(request, response);
    }

    @GetMapping()
    public ResponseEntity<?> getUsersByRole(@RequestParam("role") Long roleId) {

        List<UserResponseDto> users = userService.getUsersByRole(roleId);

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(users);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("id") Long id) {

        ApiResponse response = userService.deleteUser(id);

        return ResponseEntity.ok(response);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDto request) {

        ApiResponse response = userService.updateUser(id, request);

        return ResponseEntity.ok(response);
    }
}
