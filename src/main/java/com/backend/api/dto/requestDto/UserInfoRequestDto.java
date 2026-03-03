package com.backend.api.dto.requestDto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoRequestDto {
    private String userName;

    private String NRC;

    private String phoneNumber;

    private String address;

    private String email;

    private String DOB;

    private String gender;
}
