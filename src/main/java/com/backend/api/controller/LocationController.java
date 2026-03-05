package com.backend.api.controller;

import com.backend.api.common.response.ApiResponse;
import com.backend.api.dto.requestDto.LocationRequestDto;
import com.backend.api.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
@CrossOrigin
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/{creatorId}")
    public ResponseEntity<ApiResponse> assignLocation(
            @PathVariable Long creatorId,
            @RequestBody LocationRequestDto requestDto) {

        ApiResponse response = locationService.assignLocation(creatorId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public ResponseEntity<ApiResponse> getAllAssignLocation() {

        ApiResponse response = locationService.getAllAssignLocation();

        return ResponseEntity
                .status(response.getCode())
                .body(response);
    }
    @DeleteMapping("/{locationId}")
    public ResponseEntity<ApiResponse> deleteLocation(@PathVariable Long locationId) {

        ApiResponse response = locationService.deleteLocationById(locationId);

        return ResponseEntity.ok(response);
    }
    @PatchMapping("/{locationId}")
    public ResponseEntity<ApiResponse> updateLocation(
            @PathVariable Long locationId,
            @RequestBody LocationRequestDto requestDto) {

        ApiResponse response = locationService.updateLocation(locationId, requestDto);

        return ResponseEntity.ok(response);
    }
}