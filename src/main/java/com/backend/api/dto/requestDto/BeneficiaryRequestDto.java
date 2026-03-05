package com.backend.api.dto.requestDto;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class BeneficiaryRequestDto {

//    @NotBlank(message = "Beneficiary name is required")
//    @Size(max = 50)
    private String beneficName;

//    @NotBlank(message = "Father name is required")
//    @Size(max = 50)
    private String fatherName;

//    @NotNull(message = "Contact number is required")
    private String contact;

//    @NotNull(message = "Location ID is required")
    private Long locationId;
}