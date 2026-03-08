package com.backend.api.dto.requestDto;

import com.backend.api.utility.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DistributionRecordRequestDto {

    private Status status;
    private LocalDate distributionDate;
    private String houseHoldNrc;
    private int familyMembers;
    private int underFive;
    private int disabled;
    private String distributedItems;

    private Long beneficiaryId;
//    private Long stockId;
    private Long userId;

}
