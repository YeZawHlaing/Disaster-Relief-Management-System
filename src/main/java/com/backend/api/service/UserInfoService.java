package com.backend.api.service;

import com.backend.api.common.response.ApiResponse;
import com.backend.api.dto.requestDto.UserInfoRequestDto;
import com.backend.api.dto.responseDto.UserInfoResponseDto;
import com.backend.api.entity.UserInfo;
import com.backend.api.repository.UserInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final ModelMapper modelMapper;

    public ApiResponse createUserInfo(UserInfoRequestDto request) {


        UserInfo userInfo = modelMapper.map(request, UserInfo.class);

        UserInfo savedUserInfo = userInfoRepository.save(userInfo);

        UserInfoResponseDto responseDto =
                modelMapper.map(savedUserInfo, UserInfoResponseDto.class);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(Map.of("userInfo", responseDto))
                .message("User Information created successfully.")
                .build();
    }
}