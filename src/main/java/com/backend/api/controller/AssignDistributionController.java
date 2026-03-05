package com.backend.api.controller;

import com.backend.api.common.response.ApiResponse;
import com.backend.api.dto.requestDto.AssignDistributionRequestDto;
import com.backend.api.service.AssignDistributionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assign-distributions")
@RequiredArgsConstructor
public class AssignDistributionController {

    private final AssignDistributionService service;

    @PostMapping
    public ApiResponse create(@RequestParam Long userId,
                              @RequestBody AssignDistributionRequestDto request) {
        return service.createAssignDistribution(userId, request);
    }

    @GetMapping
    public ApiResponse getAll() {
        return service.getAllAssignDistributions();
    }

    @PatchMapping("/{id}")
    public ApiResponse update(@PathVariable Long id,
                              @RequestBody AssignDistributionRequestDto request) {
        return service.updateAssignDistribution(id, request);
    }

    @DeleteMapping("/{id}")
    public ApiResponse delete(@PathVariable Long id) {
        return service.deleteAssignDistributionById(id);
    }
}