package com.backend.api.controller;

import com.backend.api.common.response.ApiResponse;
import com.backend.api.dto.requestDto.StockInfoRequestDto;
import com.backend.api.service.StockInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockInfoController {

    private final StockInfoService stockInfoService;

    // CREATE
    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse> createStockInfo(
            @PathVariable Long userId,
            @RequestBody StockInfoRequestDto request) {

        ApiResponse response = stockInfoService.createStockInfo(userId, request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<ApiResponse> getAllStockInfo() {

        ApiResponse response = stockInfoService.getAllStockInfo();
        return ResponseEntity.status(response.getCode()).body(response);
    }

    // UPDATE
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> updateStockInfo(
            @PathVariable Long id,
            @RequestBody StockInfoRequestDto request) {

        ApiResponse response = stockInfoService.updateStockInfo(id, request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteStockInfo(@PathVariable Long id) {

        ApiResponse response = stockInfoService.deleteStockInfoById(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

}