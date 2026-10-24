package com.theater_venue_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreenDto {

    private Long id;
    private String name;
    private Integer capacity;
    private String screenType;
    private Long theaterId;
}
