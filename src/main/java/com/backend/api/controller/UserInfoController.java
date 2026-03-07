package com.backend.api.controller;

import com.backend.api.common.response.ApiResponse;
import com.backend.api.dto.requestDto.UserInfoRequestDto;
import com.backend.api.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/userInfo")
@RequiredArgsConstructor
@CrossOrigin
public class UserInfoController {

    private final UserInfoService userInfoService;

    // CREATE
    @PostMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> createUserInfo(
            @PathVariable Long userId,
            @RequestBody UserInfoRequestDto request) {
        ApiResponse response = userInfoService.createUserInfo(userId, request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<ApiResponse> getAllUserInfo() {
        ApiResponse response = userInfoService.getAllUserInfo();
        return ResponseEntity.status(response.getCode()).body(response);
    }

    // UPDATE
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUserInfo(
            @PathVariable Long id,
            @RequestBody UserInfoRequestDto request) {
        ApiResponse response = userInfoService.updateUserInfo(id, request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUserInfo(@PathVariable Long id) {
        ApiResponse response = userInfoService.deleteUserInfo(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

}