package com.theater_venue_service.services.impl;

import com.theater_venue_service.dtos.SeatDto;
import com.theater_venue_service.entity.Screen;
import com.theater_venue_service.entity.Seat;
import com.theater_venue_service.execptions.ResourceNotFoundException;
import com.theater_venue_service.repositories.ScreenRepository;
import com.theater_venue_service.repositories.SeatRepository;
import com.theater_venue_service.services.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatServiceImpl implements SeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Override
    @Transactional
    public SeatDto createSeat(SeatDto seatDto) {
        Seat seat = mapToEntity(seatDto);
        Seat savedSeat = seatRepository.save(seat);
        return mapToDto(savedSeat);
    }

    @Override
    @Transactional(readOnly = true)
    public SeatDto getSeatById(Long id) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with id: " + id));
        return mapToDto(seat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatDto> getAllSeats() {
        List<Seat> seats = seatRepository.findAll();
        return seats.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatDto> getSeatsByScreenId(Long screenId) {
        List<Seat> seats = seatRepository.findByScreenId(screenId);
        return seats.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatDto> getSeatsByScreenIdAndType(Long screenId, String seatType) {
        List<Seat> seats = seatRepository.findByScreenIdAndSeatType(screenId, seatType);
        return seats.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SeatDto updateSeat(Long id, SeatDto seatDto) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with id: " + id));

        Screen screen = screenRepository.findById(seatDto.getScreenId())
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found with id: " + seatDto.getScreenId()));

        seat.setSeatNumber(seatDto.getSeatNumber());
        seat.setRowName(seatDto.getRowName());
        seat.setSeatType(seatDto.getSeatType());
        seat.setIsWheelChairAccessible(seatDto.getIsWheelchairAccessible());
        seat.setScreen(screen);

        Seat updatedSeat = seatRepository.save(seat);
        return mapToDto(updatedSeat);
    }

    @Override
    @Transactional
    public void deleteSeat(Long id) {

        if (!seatRepository.existsById(id)) {
            throw new ResourceNotFoundException("Seat not found with id: " + id);
        }
        seatRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void createBulkSeats(List<SeatDto> seatDtos) {
        List<Seat> seats = seatDtos.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());
        seatRepository.saveAll(seats);
    }

    private Seat mapToEntity(SeatDto seatDto) {
        Seat seat = new Seat();
        seat.setId(seatDto.getId());
        seat.setSeatNumber(seatDto.getSeatNumber());
        seat.setRowName(seatDto.getRowName());
        seat.setSeatType(seatDto.getSeatType());
        seat.setIsWheelChairAccessible(seatDto.getIsWheelchairAccessible() != null ? seatDto.getIsWheelchairAccessible() : false);

        if (seatDto.getScreenId() != null) {
            Screen screen = screenRepository.findById(seatDto.getScreenId())
                    .orElseThrow(() -> new ResourceNotFoundException("Screen not found with id: " + seatDto.getScreenId()));
            seat.setScreen(screen);
        }

        return seat;
    }

    private SeatDto mapToDto(Seat seat) {
        SeatDto seatDto = new SeatDto();
        seatDto.setId(seat.getId());
        seatDto.setSeatNumber(seat.getSeatNumber());
        seatDto.setRowName(seat.getRowName());
        seatDto.setSeatType(seat.getSeatType());
        seatDto.setIsWheelchairAccessible(seat.getIsWheelChairAccessible());
        seatDto.setScreenId(seat.getScreen().getId());
        return seatDto;
    }
}
