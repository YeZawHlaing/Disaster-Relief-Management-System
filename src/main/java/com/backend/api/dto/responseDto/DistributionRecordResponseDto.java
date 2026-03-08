package com.backend.api.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DistributionRecordResponseDto {

    private Long id;
    private String status;
    private LocalDate distributionDate;
    private String houseHoldNrc;
    private int familyMembers;
    private int underFive;
    private int disabled;
    private String distributedItems;

    private BeneficiaryResponseDto beneficiary;
    private StockInfoResponseDto stock;
    private UserResponseDto user;

    private String locationName;
}