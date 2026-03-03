package com.backend.api.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {

    private Long id;

    private String email;

    private RoleResponseDto role;
}
