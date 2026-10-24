package com.theater_venue_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SeatDto {

    private Long id;
    private String seatNumber;
    private String rowName;
    private String seatType;
    private Boolean isWheelchairAccessible;
    private Long screenId;
}
