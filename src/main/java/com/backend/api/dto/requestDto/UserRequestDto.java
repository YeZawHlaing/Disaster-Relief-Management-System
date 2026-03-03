package com.backend.api.dto.requestDto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {

    private String email;

    private String password;

    private String role;



}
