package com.theater_venue_service.controllers;

import com.theater_venue_service.dtos.SeatLayoutDto;
import com.theater_venue_service.services.SeatLayoutService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seat-layouts")
@CrossOrigin
public class SeatLayoutController {

    @Autowired
    private SeatLayoutService seatLayoutService;

    @PostMapping
    public ResponseEntity<SeatLayoutDto> createSeatLayout(@Valid @RequestBody SeatLayoutDto seatLayoutDto) {
        return new ResponseEntity<>(seatLayoutService.createSeatLayout(seatLayoutDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatLayoutDto> getSeatLayoutById(@PathVariable Long id) {
        return ResponseEntity.ok(seatLayoutService.getSeatLayoutById(id));
    }

    @GetMapping("/screen/{screenId}")
    public ResponseEntity<SeatLayoutDto> getSeatLayoutByScreenId(@PathVariable Long screenId) {
        return ResponseEntity.ok(seatLayoutService.getSeatLayoutByScreenId(screenId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeatLayoutDto> updateSeatLayout(@PathVariable Long id, @Valid @RequestBody SeatLayoutDto seatLayoutDto) {
        return ResponseEntity.ok(seatLayoutService.updateSeatLayout(id, seatLayoutDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeatLayout(@PathVariable Long id) {
        seatLayoutService.deleteSeatLayout(id);
        return ResponseEntity.noContent().build();
    }
}
