package com.backend.api.service;

import com.backend.api.common.response.ApiResponse;
import com.backend.api.dto.requestDto.AssignDistributionRequestDto;
import com.backend.api.dto.responseDto.AssignDistributionResponseDto;
import com.backend.api.entity.AssignDistribution;
import com.backend.api.entity.Location;
import com.backend.api.entity.User;
import com.backend.api.repository.AssignDistributionRepository;
import com.backend.api.repository.LocationRepository;
import com.backend.api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class AssignDistributionService {

    private final AssignDistributionRepository assignDistributionRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final ModelMapper modelMapper;

    // Create AssignDistribution
    public ApiResponse createAssignDistribution(Long creatorId, AssignDistributionRequestDto request) {

        // 1️⃣ Fetch user (creator)
        User user = userRepository.findById(creatorId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // 2️⃣ Fetch location
        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new EntityNotFoundException("Location not found"));

        // 3️⃣ Create AssignDistribution entity
        AssignDistribution distribution = new AssignDistribution();
        distribution.setDistributionDate(request.getDistributionDate());
        distribution.setStatus(request.getStatus());
        distribution.setEventType(request.getEventType());
        distribution.setUser(user);
        distribution.setLocation(location);

        // 4️⃣ Save
        AssignDistribution saved = assignDistributionRepository.save(distribution);

        // 5️⃣ Map to response DTO
        AssignDistributionResponseDto responseDto =
                modelMapper.map(saved, AssignDistributionResponseDto.class);

        responseDto.setUserId(user.getId());
        responseDto.setLocationId(location.getId());

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .message("AssignDistribution created successfully")
                .data(Map.of("assignDistribution", responseDto))
                .build();
    }

    // Get all
    public ApiResponse getAllAssignDistributions() {
        List<AssignDistribution> distributions = assignDistributionRepository.findAll();

        if (distributions.isEmpty()) {
            return ApiResponse.builder()
                    .success(1)
                    .code(HttpStatus.NO_CONTENT.value())
                    .message("No AssignDistributions found")
                    .data(null)
                    .build();
        }

        List<AssignDistributionResponseDto> dtoList = distributions.stream()
                .map(d -> {
                    AssignDistributionResponseDto dto = modelMapper.map(d, AssignDistributionResponseDto.class);
                    dto.setUserId(d.getUser().getId());
                    dto.setLocationId(d.getLocation().getId());
                    dto.setLocationName(d.getLocation().getLocationName());
                    return dto;
                }).toList();

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("AssignDistributions retrieved successfully")
                .data(Map.of("assignDistributions", dtoList))
                .build();
    }

    // Update (Patch)
    public ApiResponse updateAssignDistribution(Long id, AssignDistributionRequestDto request) {
        AssignDistribution distribution = assignDistributionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AssignDistribution not found"));

        if (request.getDistributionDate() != null) {
            distribution.setDistributionDate(request.getDistributionDate());
        }

        if (request.getStatus() != null) {
            distribution.setStatus(request.getStatus());
        }

        if (request.getEventType() != null) {
            distribution.setEventType(request.getEventType());
        }

        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            distribution.setUser(user);
        }

        if (request.getLocationId() != null) {
            Location location = locationRepository.findById(request.getLocationId())
                    .orElseThrow(() -> new EntityNotFoundException("Location not found"));
            distribution.setLocation(location);
        }

        AssignDistribution updated = assignDistributionRepository.save(distribution);

        AssignDistributionResponseDto responseDto = modelMapper.map(updated, AssignDistributionResponseDto.class);
        responseDto.setUserId(updated.getUser().getId());
        responseDto.setLocationId(updated.getLocation().getId());

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("AssignDistribution updated successfully")
                .data(Map.of("assignDistribution", responseDto))
                .build();
    }

    // Delete
    public ApiResponse deleteAssignDistributionById(Long id) {
        AssignDistribution distribution = assignDistributionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AssignDistribution not found"));

        // Optional: detach relationships to avoid FK issues
        distribution.setUser(null);
        distribution.setLocation(null);

        assignDistributionRepository.flush();
        assignDistributionRepository.delete(distribution);
        assignDistributionRepository.flush();

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("AssignDistribution deleted successfully")
                .build();
    }
}