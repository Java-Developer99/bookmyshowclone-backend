package com.theater_venue_service.services;

import com.theater_venue_service.dtos.SeatLayoutDto;

public interface SeatLayoutService {

    SeatLayoutDto createSeatLayout(SeatLayoutDto seatLayoutDto);
    SeatLayoutDto getSeatLayoutById(Long id);
    SeatLayoutDto getSeatLayoutByScreenId(Long screenId);
    SeatLayoutDto updateSeatLayout(Long id, SeatLayoutDto seatLayoutDto);
    void deleteSeatLayout(Long id);
}
