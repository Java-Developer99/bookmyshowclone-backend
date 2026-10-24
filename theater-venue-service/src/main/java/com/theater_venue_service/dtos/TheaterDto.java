package com.theater_venue_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TheaterDto {

    private Long id;
    private String name;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String contactPhone;
    private String email;
    private String description;
    private Boolean isActive;
    private List<ScreenDto> screens;
    private List<AmenityDto> amenities;
}
