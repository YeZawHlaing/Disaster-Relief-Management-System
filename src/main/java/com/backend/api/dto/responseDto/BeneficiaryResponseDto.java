package com.backend.api.dto.responseDto;

import lombok.Data;

@Data
public class BeneficiaryResponseDto {

    private Long id;
    private String beneficName;
    private String fatherName;
    private String contact;
    private Long locationId;
}
