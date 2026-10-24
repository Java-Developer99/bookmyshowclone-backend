package com.userservice.dto;

import lombok.Data;

@Data
public class UserProfileRequestDto {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String dateOfBirth;
    private String address;
}
