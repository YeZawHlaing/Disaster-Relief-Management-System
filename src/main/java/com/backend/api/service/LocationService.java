package com.backend.api.service;

import com.backend.api.common.response.ApiResponse;
import com.backend.api.dto.requestDto.LocationRequestDto;
import com.backend.api.dto.responseDto.LocationResponseDto;
import com.backend.api.entity.Location;
import com.backend.api.entity.User;
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
public class LocationService {

    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ApiResponse assignLocation(Long creatorId, LocationRequestDto request) {

        // 1️⃣ Fetch staff
        User staff = userRepository.findById(request.getStaffId())
                .orElseThrow(() -> new EntityNotFoundException("FieldStaff not found"));

        // 2️⃣ Fetch creator
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new EntityNotFoundException("Creator not found"));

        // 3️⃣ Create entity manually
        Location location = new Location();
        location.setLocationName(request.getLocationName());
        location.setFieldStaff(staff);
        location.setCreator(creator);

        // 4️⃣ Save
        Location savedLocation = locationRepository.save(location);

        // 5️⃣ Map to DTO
        LocationResponseDto responseDto =
                modelMapper.map(savedLocation, LocationResponseDto.class);

        responseDto.setStaffId(staff.getId());

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(Map.of("location", responseDto))
                .message("Location assigned successfully.")
                .build();
    }

    public ApiResponse getAllAssignLocation() {

        List<Location> locations = locationRepository.findAll();

        if (locations.isEmpty()) {
            return ApiResponse.builder()
                    .success(1)
                    .code(HttpStatus.NO_CONTENT.value())
                    .message("No assigned locations found.")
                    .data(null)
                    .build();
        }

        List<LocationResponseDto> dtoList = locations.stream()
                .map(location -> {
                    LocationResponseDto dto =
                            modelMapper.map(location, LocationResponseDto.class);

                    dto.setStaffId(location.getFieldStaff().getId());
                    dto.setCreatorId(location.getCreator().getId());

                    return dto;
                })
                .toList();

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Assigned locations retrieved successfully.")
                .data(Map.of("locations", dtoList))
                .build();
    }

    public ApiResponse updateLocation(Long locationId, LocationRequestDto request) {

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found"));

        // update location name if provided
        if (request.getLocationName() != null) {
            location.setLocationName(request.getLocationName());
        }

        // update staff if provided
        if (request.getStaffId() != null) {
            User staff = userRepository.findById(request.getStaffId())
                    .orElseThrow(() -> new EntityNotFoundException("FieldStaff not found"));

            location.setFieldStaff(staff);
        }

        Location updatedLocation = locationRepository.save(location);

        LocationResponseDto responseDto =
                modelMapper.map(updatedLocation, LocationResponseDto.class);

        responseDto.setStaffId(updatedLocation.getFieldStaff().getId());

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Location updated successfully.")
                .data(Map.of("location", responseDto))
                .build();
    }
    @Transactional
    public ApiResponse deleteLocationById(Long locationId) {

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found"));

        User fieldStaff = location.getFieldStaff();
        if (fieldStaff != null) {
            fieldStaff.setAssignedLocation(null);
            location.setFieldStaff(null);
            userRepository.save(fieldStaff);
        }

        // 2️⃣ Optionally detach the creator to avoid FK issues
        location.setCreator(null);

        locationRepository.flush(); // commit detach

        locationRepository.delete(location);
        locationRepository.flush();

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Location deleted successfully")
                .build();
    }
}