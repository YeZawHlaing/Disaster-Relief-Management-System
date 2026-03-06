package com.backend.api.service;

import com.backend.api.common.response.ApiResponse;
import com.backend.api.dto.requestDto.DistributionRecordRequestDto;
import com.backend.api.dto.responseDto.DistributionRecordResponseDto;
import com.backend.api.entity.DistributionRecord;
import com.backend.api.repository.BeneficiaryRepository;
import com.backend.api.repository.DistributionRecordRepository;
import com.backend.api.repository.StockInfoRepository;
import com.backend.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DistributionRecordService {

    private final DistributionRecordRepository distributionRecordRepository;
    private final BeneficiaryRepository beneficiaryRepository;
    private final StockInfoRepository stockRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    // CREATE
    public ApiResponse createDistributionRecord(Long userId, DistributionRecordRequestDto dto) {

        DistributionRecord record = new DistributionRecord();

        record.setStatus(dto.getStatus());
        record.setDistributionDate(dto.getDistributionDate());
        record.setQuantityGiven(dto.getQuantityGiven());
        record.setUnitOfMeasure(dto.getUnitOfMeasure());
        record.setReportCreatedDate(dto.getReportCreatedDate());
        record.setEmergencyDate(dto.getEmergencyDate());
        record.setDistributedDate(dto.getDistributedDate());

        record.setBeneficiary(
                beneficiaryRepository.findById(dto.getBeneficiaryId())
                        .orElseThrow(() -> new RuntimeException("Beneficiary not found")));

        record.setStock(
                stockRepository.findById(dto.getStockId())
                        .orElseThrow(() -> new RuntimeException("Stock not found")));

        record.setUser(
                userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found")));

     DistributionRecord saveRecord=distributionRecordRepository.save(record);

        DistributionRecordResponseDto responseDto = modelMapper.map(saveRecord,DistributionRecordResponseDto.class);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .message("Distribution record created successfully.")
                .data(responseDto)
                .build();
    }


    // GET ALL
    public ApiResponse getAllDistributionRecord() {

        List<DistributionRecord> records =
                distributionRecordRepository.findAll();

        if (records.isEmpty()) {
            return ApiResponse.builder()
                    .success(1)
                    .code(HttpStatus.NO_CONTENT.value())
                    .message("No distribution records found.")
                    .data(null)
                    .build();
        }

        List<DistributionRecordResponseDto> dtoList =
                records.stream()
                        .map(record -> {
                            DistributionRecordResponseDto dto =
                                    modelMapper.map(record, DistributionRecordResponseDto.class);

                            dto.setUserId(record.getUser().getId());
                            dto.setBeneficiaryId(record.getBeneficiary().getId());
                            dto.setStockId(record.getStock().getId());

                            return dto;
                        })
                        .toList();

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Distribution records retrieved successfully.")
                .data(Map.of("distributionRecords", dtoList))
                .build();
    }


    // UPDATE
    public ApiResponse updateDistributionRecordById(Long id,
                                                    DistributionRecordRequestDto dto) {

        DistributionRecord record =
                distributionRecordRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Distribution record not found"));

        modelMapper.map(dto, record);

        record.setBeneficiary(
                beneficiaryRepository.findById(dto.getBeneficiaryId()).orElseThrow());

        record.setStock(
                stockRepository.findById(dto.getStockId()).orElseThrow());

//        record.setUser(
//                userRepository.findById(dto.getUserId()).orElseThrow());

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

        DistributionRecord record =
                distributionRecordRepository.findById(id)
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