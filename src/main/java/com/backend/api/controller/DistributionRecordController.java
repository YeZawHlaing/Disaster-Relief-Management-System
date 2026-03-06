package com.backend.api.controller;

import com.backend.api.common.response.ApiResponse;
import com.backend.api.dto.requestDto.DistributionRecordRequestDto;
import com.backend.api.service.DistributionRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/distribution")
@RequiredArgsConstructor
@CrossOrigin
public class DistributionRecordController {

    private final DistributionRecordService distributionRecordService;


    @PostMapping("/{userId}")
    public ApiResponse createDistributionRecord(
            @PathVariable Long userId,
            @RequestBody DistributionRecordRequestDto dto) {

        return distributionRecordService.createDistributionRecord(userId, dto);
    }


    @GetMapping
    public ApiResponse getAllDistributionRecord(){

        return distributionRecordService.getAllDistributionRecord();
    }


    @PutMapping("/{id}")
    public ApiResponse updateDistributionRecordById(
            @PathVariable Long id,
            @RequestBody DistributionRecordRequestDto dto){

        return distributionRecordService
                .updateDistributionRecordById(id,dto);
    }


    @DeleteMapping("/{id}")
    public ApiResponse deleteDistributionRecordById(
            @PathVariable Long id){

        return distributionRecordService
                .deleteDistributionRecordById(id);
    }

}
