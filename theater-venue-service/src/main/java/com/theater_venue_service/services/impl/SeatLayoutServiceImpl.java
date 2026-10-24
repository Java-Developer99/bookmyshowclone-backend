package com.theater_venue_service.services.impl;

import com.theater_venue_service.dtos.SeatLayoutDto;
import com.theater_venue_service.entity.Screen;
import com.theater_venue_service.entity.SeatLayout;
import com.theater_venue_service.execptions.ResourceNotFoundException;
import com.theater_venue_service.repositories.ScreenRepository;
import com.theater_venue_service.repositories.SeatLayoutRepository;
import com.theater_venue_service.services.SeatLayoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeatLayoutServiceImpl implements SeatLayoutService {

    @Autowired
    private SeatLayoutRepository seatLayoutRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Override
    @Transactional
    public SeatLayoutDto createSeatLayout(SeatLayoutDto seatLayoutDto) {
        SeatLayout seatLayout = mapToEntity(seatLayoutDto);
        SeatLayout savedSeatLayout = seatLayoutRepository.save(seatLayout);
        return mapToDto(savedSeatLayout);
    }

    @Override
    @Transactional(readOnly = true)
    public SeatLayoutDto getSeatLayoutById(Long id) {
        SeatLayout seatLayout = seatLayoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat layout not found with id: " + id));
        return mapToDto(seatLayout);
    }

    @Override
    @Transactional(readOnly = true)
    public SeatLayoutDto getSeatLayoutByScreenId(Long screenId) {
        SeatLayout seatLayout = seatLayoutRepository.findByScreenId(screenId)
                .orElseThrow(() -> new ResourceNotFoundException("Seat layout not found for screen id: " + screenId));
        return mapToDto(seatLayout);
    }

    @Override
    @Transactional
    public SeatLayoutDto updateSeatLayout(Long id, SeatLayoutDto seatLayoutDto) {
        SeatLayout seatLayout = seatLayoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat layout not found with id: " + id));

        Screen screen = screenRepository.findById(seatLayoutDto.getScreenId())
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found with id: " + seatLayoutDto.getScreenId()));

        seatLayout.setScreen(screen);
        seatLayout.setLayoutJson(seatLayoutDto.getLayoutJson());
        seatLayout.setRowCount(seatLayoutDto.getRowCount());
        seatLayout.setColumnCount(seatLayoutDto.getColumnCount());

        SeatLayout updatedSeatLayout = seatLayoutRepository.save(seatLayout);
        return mapToDto(updatedSeatLayout);
    }

    @Override
    @Transactional
    public void deleteSeatLayout(Long id) {

        if (!seatLayoutRepository.existsById(id)) {
            throw new ResourceNotFoundException("Seat layout not found with id: " + id);
        }
        seatLayoutRepository.deleteById(id);
    }

    private SeatLayout mapToEntity(SeatLayoutDto seatLayoutDto) {
        SeatLayout seatLayout = new SeatLayout();
        seatLayout.setId(seatLayoutDto.getId());
        seatLayout.setLayoutJson(seatLayoutDto.getLayoutJson());
        seatLayout.setRowCount(seatLayoutDto.getRowCount());
        seatLayout.setColumnCount(seatLayoutDto.getColumnCount());

        if (seatLayoutDto.getScreenId() != null) {
            Screen screen = screenRepository.findById(seatLayoutDto.getScreenId())
                    .orElseThrow(() -> new ResourceNotFoundException("Screen not found with id: " + seatLayoutDto.getScreenId()));
            seatLayout.setScreen(screen);
        }

        return seatLayout;
    }

    private SeatLayoutDto mapToDto(SeatLayout seatLayout) {
        SeatLayoutDto seatLayoutDto = new SeatLayoutDto();
        seatLayoutDto.setId(seatLayout.getId());
        seatLayoutDto.setLayoutJson(seatLayout.getLayoutJson());
        seatLayoutDto.setRowCount(seatLayout.getRowCount());
        seatLayoutDto.setColumnCount(seatLayout.getColumnCount());
        seatLayoutDto.setScreenId(seatLayout.getScreen().getId());
        return seatLayoutDto;
    }
}
