package com.backend.api.controller;

import com.backend.api.common.response.ApiResponse;
import com.backend.api.dto.requestDto.UserInfoRequestDto;
import com.backend.api.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/userInfomation")
@RequiredArgsConstructor
@CrossOrigin
public class UserInfoController {

    private final UserInfoService userInfoService;

    @PostMapping
    public ResponseEntity<ApiResponse> createUserInfo(
             @RequestBody UserInfoRequestDto request) {

        ApiResponse response = userInfoService.createUserInfo(request);

        return ResponseEntity.ok(response);
    }
}
