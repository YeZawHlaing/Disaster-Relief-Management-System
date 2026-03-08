package com.backend.api.dto.requestDto;

import com.backend.api.utility.enums.Status;
import lombok.Data;

@Data
public class UpdateStatusRequestDto {

    private Status status;
}
