package com.backend.api.controller;

import com.backend.api.common.response.ApiResponse;
import com.backend.api.dto.requestDto.BeneficiaryRequestDto;
import com.backend.api.service.BeneficiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/beneficiaries")
@RequiredArgsConstructor
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    @PostMapping("/{locationId}")
    public ResponseEntity<ApiResponse> createBeneficiary(
            @PathVariable Long locationId,
            @RequestBody BeneficiaryRequestDto dto) {

        ApiResponse response = beneficiaryService.createBeneficiary(locationId, dto);

        return ResponseEntity.status(response.getCode()).body(response);
    }
    @GetMapping
    public ResponseEntity<ApiResponse> getAllBeneficiaries() {
        ApiResponse response = beneficiaryService.getAllBeneficiaries();
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> updateBeneficiary(
            @PathVariable Long id,
            @RequestBody BeneficiaryRequestDto dto) {

        ApiResponse response = beneficiaryService.updateBeneficiaryById(id, dto);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBeneficiary(@PathVariable Long id) {

        ApiResponse response = beneficiaryService.deleteBeneficiaryById(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}