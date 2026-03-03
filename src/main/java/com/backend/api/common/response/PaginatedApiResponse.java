package com.backend.api.common.response;

import java.util.List;
import com.backend.api.common.pagination.PaginationMeta;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginatedApiResponse<T>  {
    private int success;
    private int code;
    private String message;
    private PaginationMeta meta;
    private List<T> data;
}

