package com.theater_venue_service.services;

import com.theater_venue_service.dtos.SeatDto;

import java.util.List;

public interface SeatService {

    SeatDto createSeat(SeatDto seatDto);
    SeatDto getSeatById(Long id);
    List<SeatDto> getAllSeats();
    List<SeatDto> getSeatsByScreenId(Long screenId);
    List<SeatDto> getSeatsByScreenIdAndType(Long screenId, String seatType);
    SeatDto updateSeat(Long id, SeatDto seatDto);
    void deleteSeat(Long id);
    void createBulkSeats(List<SeatDto> seatDtos);
}
