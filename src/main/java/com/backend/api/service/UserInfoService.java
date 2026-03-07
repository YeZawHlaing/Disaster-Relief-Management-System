package com.backend.api.service;

import com.backend.api.common.response.ApiResponse;
import com.backend.api.dto.requestDto.UserInfoRequestDto;
import com.backend.api.dto.responseDto.UserInfoResponseDto;
import com.backend.api.entity.User;
import com.backend.api.entity.UserInfo;
import com.backend.api.repository.UserInfoRepository;
import com.backend.api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    // CREATE
    public ApiResponse createUserInfo(Long userId, UserInfoRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        UserInfo userInfo = modelMapper.map(request, UserInfo.class);
        userInfo.setUser(user);

        UserInfo saved = userInfoRepository.save(userInfo);

        UserInfoResponseDto response = modelMapper.map(saved, UserInfoResponseDto.class);
        response.setUserId(user.getId());

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .message("User information created successfully")
                .data(Map.of("userInfo", response))
                .build();
    }

    // GET ALL
    public ApiResponse getAllUserInfo() {
        List<UserInfo> list = userInfoRepository.findAll();

        if (list.isEmpty()) {
            return ApiResponse.builder()
                    .success(1)
                    .code(HttpStatus.NO_CONTENT.value())
                    .message("No UserInfo found")
                    .data(null)
                    .build();
        }

        List<UserInfoResponseDto> dtoList = list.stream().map(userInfo -> {
            UserInfoResponseDto dto = modelMapper.map(userInfo, UserInfoResponseDto.class);
            dto.setUserId(userInfo.getUser().getId());
            return dto;
        }).collect(Collectors.toList());

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("UserInfo retrieved successfully")
                .data(Map.of("userInfos", dtoList))
                .build();
    }

    // UPDATE
    public ApiResponse updateUserInfo(Long id, UserInfoRequestDto request) {
        UserInfo userInfo = userInfoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UserInfo not found"));

        modelMapper.map(request, userInfo);

        UserInfo updated = userInfoRepository.save(userInfo);

        UserInfoResponseDto response = modelMapper.map(updated, UserInfoResponseDto.class);
        response.setUserId(updated.getUser().getId());

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("UserInfo updated successfully")
                .data(Map.of("userInfo", response))
                .build();
    }
    @Transactional
    public ApiResponse deleteUserInfo(Long id) {

        UserInfo userInfo = userInfoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UserInfo not found"));

        User user = userInfo.getUser();

        if (user != null) {
//            distributionRecordRepository.deleteByUserId(user.getId());
            userRepository.delete(user);
        }

        userInfoRepository.delete(userInfo);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("UserInfo deleted successfully")
                .build();
    }

}