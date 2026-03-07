package com.backend.api.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponseDto {

    private Long id;

    private String userName;

    private String NRC;

    private String phoneNumber;

    private String address;

    private String email;

    private String DOB;

    private String gender;

    private Long UserId;
}
