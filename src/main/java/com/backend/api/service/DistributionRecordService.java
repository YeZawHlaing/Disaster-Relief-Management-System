package com.backend.api.service;

import com.backend.api.common.response.ApiResponse;
import com.backend.api.common.response.ResponseUtils;
import com.backend.api.dto.requestDto.DistributionRecordRequestDto;
import com.backend.api.dto.requestDto.UpdateStatusRequestDto;
import com.backend.api.dto.responseDto.BeneficiaryResponseDto;
import com.backend.api.dto.responseDto.DistributionRecordResponseDto;
import com.backend.api.dto.responseDto.RoleResponseDto;
import com.backend.api.dto.responseDto.UserResponseDto;
import com.backend.api.entity.*;
import com.backend.api.repository.*;
import com.backend.api.utility.enums.Status;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.poi.ss.usermodel.*;
import java.io.*;


import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DistributionRecordService {

    private final DistributionRecordRepository distributionRecordRepository;
    private final BeneficiaryRepository beneficiaryRepository;
    private final StockInfoRepository stockRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final LocationRepository locationRepository;
    // CREATE

    public DistributionRecord createDistributionRecord(Long userId, DistributionRecordRequestDto dto) {
        // Fetch existing Beneficiary
        System.out.println(dto.getBeneficiaryId());
        Beneficiary beneficiary = beneficiaryRepository.findById(dto.getBeneficiaryId())
                .orElseThrow(() -> new RuntimeException("Beneficiary not found"));

//        // Fetch existing StockInfo
//        StockInfo stockInfo = stockRepository.findById(dto.getStockId())
//                .orElseThrow(() -> new RuntimeException("StockInfo not found"));

        // Fetch existing User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Map DTO to entity manually
        DistributionRecord record = new DistributionRecord();
        record.setStatus(dto.getStatus());
        record.setDistributionDate(dto.getDistributionDate());
        record.setHouseHoldNrc(dto.getHouseHoldNrc());
        record.setFamilyMembers(dto.getFamilyMembers());
        record.setUnderFive(dto.getUnderFive());
        record.setDisabled(dto.getDisabled());
        record.setDistributedItems(dto.getDistributedItems());

        // Set relationships
        record.setBeneficiary(beneficiary);
//        record.setStock(stockInfo);
        record.setUser(user);

        // Save to DB
        return distributionRecordRepository.save(record);
    }
    // GET ALL
    public ApiResponse getAllDistributionRecords() {
        List<DistributionRecord> records = distributionRecordRepository.findAll();

        if (records.isEmpty()) {
            return ApiResponse.builder()
                    .success(1)
                    .code(HttpStatus.NO_CONTENT.value())
                    .message("No distribution records found.")
                    .data(null)
                    .build();
        }

        // Fetch all beneficiaries once
        Map<Long, Beneficiary> beneficiaryMap = beneficiaryRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Beneficiary::getId, b -> b));

        List<DistributionRecordResponseDto> dtoList = records.stream()
                .map(record -> {
                    DistributionRecordResponseDto dto = modelMapper.map(record, DistributionRecordResponseDto.class);

                    // Get the beneficiary for this record
                    Beneficiary beneficiary = beneficiaryMap.get(record.getBeneficiary().getId());
                    if (beneficiary != null && beneficiary.getLocation() != null) {
                        dto.setLocationName(beneficiary.getLocation().getLocationName());
                    }

                    return dto;
                })
                .collect(Collectors.toList());

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Distribution records retrieved successfully.")
                .data(Map.of("distributionRecords", dtoList))
                .build();
    }
    // UPDATE
    public ApiResponse updateDistributionRecordById(Long id, DistributionRecordRequestDto dto) {

        DistributionRecord record = distributionRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Distribution record not found"));

        // Manual DTO mapping
        record.setStatus(dto.getStatus());
        record.setDistributionDate(dto.getDistributionDate());
        record.setHouseHoldNrc(dto.getHouseHoldNrc());
        record.setFamilyMembers(dto.getFamilyMembers());
        record.setUnderFive(dto.getUnderFive());
        record.setDisabled(dto.getDisabled());
        record.setDistributedItems(dto.getDistributedItems());

        // Update beneficiary
        if (dto.getBeneficiaryId() != null) {
            Beneficiary beneficiary = beneficiaryRepository.findById(dto.getBeneficiaryId())
                    .orElseThrow(() -> new RuntimeException("Beneficiary not found"));
            record.setBeneficiary(beneficiary);
        }

        // Update user
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            record.setUser(user);
        }

        distributionRecordRepository.save(record);

        DistributionRecordResponseDto responseDto = new DistributionRecordResponseDto();
        responseDto.setId(record.getId());
        responseDto.setStatus(String.valueOf(record.getStatus()));
        responseDto.setDistributionDate(record.getDistributionDate());
        responseDto.setHouseHoldNrc(record.getHouseHoldNrc());
        responseDto.setFamilyMembers(record.getFamilyMembers());
        responseDto.setUnderFive(record.getUnderFive());
        responseDto.setDisabled(record.getDisabled());
        responseDto.setDistributedItems(record.getDistributedItems());

// Map Beneficiary
        BeneficiaryResponseDto benDto = new BeneficiaryResponseDto();
        benDto.setId(record.getBeneficiary().getId());
        benDto.setBeneficName(record.getBeneficiary().getBeneficName());
// map only the fields you need, **avoid recursive links**
        responseDto.setBeneficiary(benDto);

// Map User
        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(record.getUser().getId());
        userDto.setEmail(record.getUser().getEmail());
// do NOT set role.users list, just role info
        RoleResponseDto roleDto = new RoleResponseDto();
        roleDto.setId(record.getUser().getRole().getId());
        roleDto.setName(record.getUser().getRole().getName());
        userDto.setRole(roleDto);

        responseDto.setUser(userDto);

        return ResponseUtils.success("Distribution record updated successfully", responseDto);
    }
    // DELETE
    public ApiResponse deleteDistributionRecordById(Long id) {
        DistributionRecord record = distributionRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Distribution record not found"));

        distributionRecordRepository.delete(record);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Distribution record deleted successfully.")
                .data(null)
                .build();
    }
    // status
    // UPDATE
    public ApiResponse updateStatus(Long id, UpdateStatusRequestDto dto) {

        DistributionRecord record = distributionRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Distribution record not found"));

        // Manual DTO mapping
        record.setStatus(dto.getStatus());


        distributionRecordRepository.save(record);

        return ResponseUtils.success("Distribution status updated successfully",record);
    }
}