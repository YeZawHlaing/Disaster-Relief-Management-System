package com.backend.api.dto.responseDto;

import com.backend.api.utility.enums.EventType;
import com.backend.api.utility.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockInfoResponseDto {

    private Long id;

    private EventType eventType;

    private String itemName;

    private String itemDescription;

    private Type type;

    private Double quantity;

    private String unitOfMeasure;

    private String storageLocation;

    private LocalDate manufacturedDate;

    private LocalDate expiriedDate;

    private LocalDateTime createdDate;

    private Long userId;

}