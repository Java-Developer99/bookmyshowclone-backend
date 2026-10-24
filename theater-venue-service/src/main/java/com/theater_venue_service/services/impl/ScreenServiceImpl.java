package com.theater_venue_service.services.impl;

import com.theater_venue_service.dtos.ScreenDto;
import com.theater_venue_service.entity.Screen;
import com.theater_venue_service.entity.Theater;
import com.theater_venue_service.execptions.ResourceNotFoundException;
import com.theater_venue_service.repositories.ScreenRepository;
import com.theater_venue_service.repositories.TheaterRepository;
import com.theater_venue_service.services.ScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScreenServiceImpl implements ScreenService {

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    @Override
    @Transactional
    public ScreenDto createScreen(ScreenDto screenDto) {
        Screen screen = mapToEntity(screenDto);
        Screen savedScreen = screenRepository.save(screen);
        return mapToDto(savedScreen);
    }

    @Override
    @Transactional(readOnly = true)
    public ScreenDto getScreenById(Long id) {
        Screen screen = screenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found with id: " + id));
        return mapToDto(screen);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScreenDto> getAllScreens() {
        List<Screen> screens = screenRepository.findAll();
        return screens.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScreenDto> getScreensByTheaterId(Long theaterId) {
        List<Screen> screens = screenRepository.findByTheaterId(theaterId);
        return screens.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScreenDto> getScreensByType(String screenType) {
        List<Screen> screens = screenRepository.findByScreenType(screenType);
        return screens.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ScreenDto updateScreen(Long id, ScreenDto screenDto) {
        Screen screen = screenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found with id: " + id));

        Theater theater = theaterRepository.findById(screenDto.getTheaterId())
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + screenDto.getTheaterId()));

        screen.setName(screenDto.getName());
        screen.setCapacity(screenDto.getCapacity());
        screen.setScreenType(screenDto.getScreenType());
        screen.setTheater(theater);

        Screen updatedScreen = screenRepository.save(screen);
        return mapToDto(updatedScreen);
    }


    @Override
    @Transactional
    public void deleteScreen(Long id) {

        if (!screenRepository.existsById(id)) {
            throw new ResourceNotFoundException("Screen not found with id: " + id);
        }
        screenRepository.deleteById(id);

    }

    private Screen mapToEntity(ScreenDto screenDto) {
        Screen screen = new Screen();
        screen.setId(screenDto.getId());
        screen.setName(screenDto.getName());
        screen.setCapacity(screenDto.getCapacity());
        screen.setScreenType(screenDto.getScreenType());

        if (screenDto.getTheaterId() != null) {
            Theater theater = theaterRepository.findById(screenDto.getTheaterId())
                    .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + screenDto.getTheaterId()));
            screen.setTheater(theater);
        }

        return screen;
    }

    private ScreenDto mapToDto(Screen screen) {
        ScreenDto screenDto = new ScreenDto();
        screenDto.setId(screen.getId());
        screenDto.setName(screen.getName());
        screenDto.setCapacity(screen.getCapacity());
        screenDto.setScreenType(screen.getScreenType());
        screenDto.setTheaterId(screen.getTheater().getId());
        return screenDto;
    }
}
