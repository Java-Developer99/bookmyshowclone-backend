package com.theater_venue_service.services;

import com.theater_venue_service.dtos.TheaterDto;

import java.util.List;

public interface TheaterService {

    TheaterDto createTheater(TheaterDto theaterDto);
    TheaterDto getTheaterById(Long id);
    List<TheaterDto> getAllTheaters();
    List<TheaterDto> getTheatersByCity(String city);
    TheaterDto updateTheater(Long id, TheaterDto theaterDto);
    void deleteTheater(Long id);
    List<TheaterDto> getActiveTheaters();
    List<TheaterDto> getActiveTheatersByCity(String city);
}
