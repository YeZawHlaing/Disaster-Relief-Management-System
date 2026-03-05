package com.backend.api.service;

import com.backend.api.common.response.ApiResponse;
import com.backend.api.dto.requestDto.BeneficiaryRequestDto;
import com.backend.api.dto.responseDto.BeneficiaryResponseDto;
import com.backend.api.entity.Beneficiary;
import com.backend.api.entity.Location;
import com.backend.api.repository.BeneficiaryRepository;
import com.backend.api.repository.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final LocationRepository locationRepository;
    private final ModelMapper modelMapper;

    public ApiResponse createBeneficiary(Long locationId, BeneficiaryRequestDto dto) {

        // 1️⃣ Check duplicate contact
        if (beneficiaryRepository.existsByContact((dto.getContact()))) {
            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.CONFLICT.value())
                    .message("Beneficiary already exists with this contact")
                    .build();
        }

        // 2️⃣ Fetch location
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found"));

        // 3️⃣ Convert DTO → Entity
        Beneficiary beneficiary = modelMapper.map(dto, Beneficiary.class);
        beneficiary.setLocation(location);

        // 4️⃣ Save entity
        Beneficiary savedBeneficiary = beneficiaryRepository.save(beneficiary);

        // 5️⃣ Convert Entity → ResponseDTO
        BeneficiaryResponseDto responseDto = modelMapper.map(savedBeneficiary, BeneficiaryResponseDto.class);
        responseDto.setLocationId(location.getId());

        // 6️⃣ Return response
        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .message("Beneficiary created successfully")
                .data(responseDto)
                .build();
    }
    // -------------------- GET ALL --------------------
    public ApiResponse getAllBeneficiaries() {

        List<Beneficiary> beneficiaries = beneficiaryRepository.findAll();

        if (beneficiaries.isEmpty()) {
            return ApiResponse.builder()
                    .success(1)
                    .code(HttpStatus.NO_CONTENT.value())
                    .message("No beneficiaries found")
                    .build();
        }

        List<BeneficiaryResponseDto> dtoList = beneficiaries.stream()
                .map(b -> {
                    BeneficiaryResponseDto dto =
                            modelMapper.map(b, BeneficiaryResponseDto.class);
                    dto.setLocationId(b.getLocation().getId());
                    return dto;
                }).toList();

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Beneficiaries retrieved successfully")
                .data(Map.of("beneficiaries", dtoList))
                .build();
    }


    // -------------------- UPDATE (PATCH) --------------------
    public ApiResponse updateBeneficiaryById(Long id, BeneficiaryRequestDto dto) {

        Beneficiary beneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Beneficiary not found"));

        // update only provided fields
        if (dto.getBeneficName() != null) {
            beneficiary.setBeneficName(dto.getBeneficName());
        }

        if (dto.getFatherName() != null) {
            beneficiary.setFatherName(dto.getFatherName());
        }

        if (dto.getContact() != null) {
            beneficiary.setContact(dto.getContact());
        }

        Beneficiary updatedBeneficiary =
                beneficiaryRepository.save(beneficiary);

        BeneficiaryResponseDto responseDto =
                modelMapper.map(updatedBeneficiary, BeneficiaryResponseDto.class);

        responseDto.setLocationId(
                updatedBeneficiary.getLocation().getId());

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Beneficiary updated successfully")
                .data(responseDto)
                .build();
    }


    // -------------------- DELETE --------------------
    public ApiResponse deleteBeneficiaryById(Long id) {

        Beneficiary beneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Beneficiary not found"));

        beneficiaryRepository.delete(beneficiary);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Beneficiary deleted successfully")
                .build();
    }
}