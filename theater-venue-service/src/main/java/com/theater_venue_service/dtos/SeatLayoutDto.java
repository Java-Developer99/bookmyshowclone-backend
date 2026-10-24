package com.theater_venue_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatLayoutDto {

    private Long id;
    private Long screenId;
    private String layoutJson;
    private Integer rowCount;
    private Integer columnCount;
}
