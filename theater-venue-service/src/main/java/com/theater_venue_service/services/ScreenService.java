package com.theater_venue_service.services;

import com.theater_venue_service.dtos.ScreenDto;

import java.util.List;

public interface ScreenService {

    ScreenDto createScreen(ScreenDto screenDto);
    ScreenDto getScreenById(Long id);
    List<ScreenDto> getAllScreens();
    List<ScreenDto> getScreensByTheaterId(Long theaterId);
    List<ScreenDto> getScreensByType(String screenType);
    ScreenDto updateScreen(Long id, ScreenDto screenDto);
    void deleteScreen(Long id);
}
