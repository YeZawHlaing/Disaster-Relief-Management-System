package com.backend.api.dto.requestDto;

import com.backend.api.utility.enums.EventType;
import com.backend.api.utility.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AssignDistributionRequestDto {

    private LocalDateTime distributionDate;

    private Status status;

    private EventType eventType;

    private Long userId;

    private Long locationId;
}
