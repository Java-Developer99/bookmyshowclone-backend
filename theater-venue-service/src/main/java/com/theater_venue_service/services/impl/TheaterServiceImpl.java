package com.theater_venue_service.services.impl;

import com.theater_venue_service.dtos.AmenityDto;
import com.theater_venue_service.dtos.ScreenDto;
import com.theater_venue_service.dtos.TheaterDto;
import com.theater_venue_service.entity.Amenity;
import com.theater_venue_service.entity.Screen;
import com.theater_venue_service.entity.Theater;
import com.theater_venue_service.execptions.ResourceNotFoundException;
import com.theater_venue_service.repositories.TheaterRepository;
import com.theater_venue_service.services.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TheaterServiceImpl implements TheaterService {

    @Autowired
    private TheaterRepository theaterRepository;

    @Override
    @Transactional
    public TheaterDto createTheater(TheaterDto theaterDto) {
        Theater theater=mapToEntity(theaterDto);
        Theater savedTheater=theaterRepository.save(theater);
        return mapToDto(savedTheater);
    }

    @Override
    @Transactional(readOnly = true)
    public TheaterDto getTheaterById(Long id) {
        Theater theater=theaterRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Theater not found with id:"+ id));
        return mapToDto(theater);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TheaterDto> getAllTheaters() {
        List<Theater> theaters=theaterRepository.findAll();
        return theaters.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TheaterDto> getTheatersByCity(String city) {
        List<Theater> theaters=theaterRepository.findByCity(city);
        return theaters.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TheaterDto updateTheater(Long id, TheaterDto theaterDto) {
        Theater theater=theaterRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Theater not found with id:"+ id));
        theater.setName(theaterDto.getName());
        theater.setAddress(theaterDto.getAddress());
        theater.setCity(theaterDto.getCity());
        theater.setState(theaterDto.getState());
        theater.setZipCode(theaterDto.getZipCode());
        theater.setContactPhone(theaterDto.getContactPhone());
        theater.setEmail(theaterDto.getEmail());
        theater.setDescription(theaterDto.getDescription());
        theater.setIsActive(theaterDto.getIsActive());

        Theater updatedTheater = theaterRepository.save(theater);
        return mapToDto(updatedTheater);
    }

    // This method will perform delete operation and delete particular theater from given id
    @Override
    @Transactional
    public void deleteTheater(Long id) {
        if (!theaterRepository.existsById(id)) {
            throw new ResourceNotFoundException("Theater not found with id: " + id);
        }
        theaterRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TheaterDto> getActiveTheaters() {
        List<Theater> theaters=theaterRepository.findByIsActive(true);
        return theaters.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TheaterDto> getActiveTheatersByCity(String city) {
        List<Theater> theaters=theaterRepository.findByCityAndIsActive(city,true);
        return theaters.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private Theater mapToEntity(TheaterDto theaterDto) {
        Theater theater = new Theater();
        theater.setId(theaterDto.getId());
        theater.setName(theaterDto.getName());
        theater.setAddress(theaterDto.getAddress());
        theater.setCity(theaterDto.getCity());
        theater.setState(theaterDto.getState());
        theater.setZipCode(theaterDto.getZipCode());
        theater.setContactPhone(theaterDto.getContactPhone());
        theater.setEmail(theaterDto.getEmail());
        theater.setDescription(theaterDto.getDescription());
        theater.setIsActive(theaterDto.getIsActive() != null ? theaterDto.getIsActive() : true);
        return theater;
    }

    private TheaterDto mapToDto(Theater theater) {
        TheaterDto theaterDto = new TheaterDto();
        theaterDto.setId(theater.getId());
        theaterDto.setName(theater.getName());
        theaterDto.setAddress(theater.getAddress());
        theaterDto.setCity(theater.getCity());
        theaterDto.setState(theater.getState());
        theaterDto.setZipCode(theater.getZipCode());
        theaterDto.setContactPhone(theater.getContactPhone());
        theaterDto.setEmail(theater.getEmail());
        theaterDto.setDescription(theater.getDescription());
        theaterDto.setIsActive(theater.getIsActive());

        // Map screens
        List<ScreenDto> screenDtos = theater.getScreens().stream()
                .map(this::mapScreenToDto)
                .collect(Collectors.toList());
        theaterDto.setScreens(screenDtos);

        // Map amenities
        List<AmenityDto> amenityDtos = theater.getAmenities().stream()
                .map(this::mapAmenityToDto)
                .collect(Collectors.toList());
        theaterDto.setAmenities(amenityDtos);

        return theaterDto;
    }

    private ScreenDto mapScreenToDto(Screen screen) {
        ScreenDto screenDto = new ScreenDto();
        screenDto.setId(screen.getId());
        screenDto.setName(screen.getName());
        screenDto.setCapacity(screen.getCapacity());
        screenDto.setScreenType(screen.getScreenType());
        screenDto.setTheaterId(screen.getTheater().getId());
        return screenDto;
    }

    private AmenityDto mapAmenityToDto(Amenity amenity) {
        AmenityDto amenityDto = new AmenityDto();
        amenityDto.setId(amenity.getId());
        amenityDto.setName(amenity.getName());
        amenityDto.setDescription(amenity.getDescription());
        amenityDto.setTheaterId(amenity.getTheater().getId());
        return amenityDto;
    }
}
