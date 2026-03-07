package com.backend.api.service;

import com.backend.api.common.response.ApiResponse;
import com.backend.api.dto.requestDto.DistributionRecordRequestDto;
import com.backend.api.dto.responseDto.DistributionRecordResponseDto;
import com.backend.api.entity.Beneficiary;
import com.backend.api.entity.DistributionRecord;
import com.backend.api.entity.StockInfo;
import com.backend.api.entity.User;
import com.backend.api.repository.BeneficiaryRepository;
import com.backend.api.repository.DistributionRecordRepository;
import com.backend.api.repository.StockInfoRepository;
import com.backend.api.repository.UserRepository;
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

    // CREATE

    public DistributionRecord createDistributionRecord(Long userId, DistributionRecordRequestDto dto) {
        // Fetch existing Beneficiary
        System.out.println(dto.getBeneficiaryId());
        Beneficiary beneficiary = beneficiaryRepository.findById(dto.getBeneficiaryId())
                .orElseThrow(() -> new RuntimeException("Beneficiary not found"));

        // Fetch existing StockInfo
        StockInfo stockInfo = stockRepository.findById(dto.getStockId())
                .orElseThrow(() -> new RuntimeException("StockInfo not found"));

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
        record.setStock(stockInfo);
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

        List<DistributionRecordResponseDto> dtoList = records.stream()
                .map(record -> modelMapper.map(record, DistributionRecordResponseDto.class))
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

        // Map basic fields from DTO to existing entity
        modelMapper.map(dto, record);

        // Update relations
        Beneficiary beneficiary = beneficiaryRepository.findById(dto.getBeneficiaryId())
                .orElseThrow(() -> new RuntimeException("Beneficiary not found"));
        record.setBeneficiary(beneficiary);

        StockInfo stock = stockRepository.findById(dto.getStockId())
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        record.setStock(stock);

        distributionRecordRepository.save(record);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Distribution record updated successfully.")
                .data(null)
                .build();
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
}