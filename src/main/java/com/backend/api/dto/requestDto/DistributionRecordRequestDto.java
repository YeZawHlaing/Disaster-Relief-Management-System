package com.backend.api.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DistributionRecordRequestDto {

    private Long userId;
    private Long beneficiaryId;
    private Long stockId;

    private String status;
    private LocalDate distributionDate;
    private Double quantityGiven;
    private String unitOfMeasure;

    private LocalDate reportCreatedDate;
    private LocalDate emergencyDate;
    private LocalDate distributedDate;

}
