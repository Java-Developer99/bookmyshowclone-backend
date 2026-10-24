package com.theater_venue_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmenityDto {

    private Long id;
    private String name;
    private String description;
    private Long theaterId;
}
