package com.backend.api.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationResponseDto {

    private Long id;
    private String locationName;
    private Long staffId;
    private Long creatorId;
}
