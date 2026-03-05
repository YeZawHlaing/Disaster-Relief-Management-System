package com.backend.api.service;

import com.backend.api.common.response.ApiResponse;
import com.backend.api.dto.requestDto.StockInfoRequestDto;
import com.backend.api.dto.responseDto.StockInfoResponseDto;
import com.backend.api.entity.StockInfo;
import com.backend.api.entity.User;
import com.backend.api.repository.StockInfoRepository;
import com.backend.api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class StockInfoService {

    private final StockInfoRepository stockInfoRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    // CREATE
    public ApiResponse createStockInfo(Long userId, StockInfoRequestDto request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Map RequestDTO -> Entity
        StockInfo stock = modelMapper.map(request, StockInfo.class);

        stock.setUser(user);
        stock.setCreatedDate(LocalDateTime.now());

        StockInfo saved = stockInfoRepository.save(stock);

        // Map Entity -> ResponseDTO
        StockInfoResponseDto response = modelMapper.map(saved, StockInfoResponseDto.class);
        response.setUserId(user.getId());

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .message("Stock created successfully")
                .data(Map.of("stockInfo", response))
                .build();
    }

    // GET ALL
    public ApiResponse getAllStockInfo() {

        List<StockInfo> stockList = stockInfoRepository.findAll();

        if (stockList.isEmpty()) {
            return ApiResponse.builder()
                    .success(1)
                    .code(HttpStatus.NO_CONTENT.value())
                    .message("No StockInfo found")
                    .data(null)
                    .build();
        }

        List<StockInfoResponseDto> dtoList = stockList.stream()
                .map(stock -> {
                    StockInfoResponseDto dto =
                            modelMapper.map(stock, StockInfoResponseDto.class);
                    dto.setUserId(stock.getUser().getId());
                    return dto;
                }).toList();

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("StockInfo retrieved successfully")
                .data(Map.of("stockInfos", dtoList))
                .build();
    }

    // UPDATE (PATCH)
    public ApiResponse updateStockInfo(Long id, StockInfoRequestDto request) {

        StockInfo stock = stockInfoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("StockInfo not found"));

        // Map request fields to existing entity
        modelMapper.map(request, stock);

        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            stock.setUser(user);
        }

        StockInfo updated = stockInfoRepository.save(stock);

        StockInfoResponseDto response =
                modelMapper.map(updated, StockInfoResponseDto.class);

        response.setUserId(updated.getUser().getId());

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("StockInfo updated successfully")
                .data(Map.of("stockInfo", response))
                .build();
    }

    // DELETE
    public ApiResponse deleteStockInfoById(Long id) {

        StockInfo stock = stockInfoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("StockInfo not found"));

        stockInfoRepository.delete(stock);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("StockInfo deleted successfully")
                .build();
    }
}