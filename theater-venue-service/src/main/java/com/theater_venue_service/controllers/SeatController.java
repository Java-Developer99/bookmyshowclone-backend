package com.theater_venue_service.controllers;

import com.theater_venue_service.dtos.SeatDto;
import com.theater_venue_service.services.SeatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@CrossOrigin
public class SeatController {

    @Autowired
    private SeatService seatService;

    @PostMapping
    public ResponseEntity<SeatDto> createSeat(@Valid @RequestBody SeatDto seatDto) {
        return new ResponseEntity<>(seatService.createSeat(seatDto), HttpStatus.CREATED);
    }

    @PostMapping("/bulk")
    public ResponseEntity<Void> createBulkSeats(@Valid @RequestBody List<SeatDto> seatDtos) {
        seatService.createBulkSeats(seatDtos);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatDto> getSeatById(@PathVariable Long id) {
        return ResponseEntity.ok(seatService.getSeatById(id));
    }

    @GetMapping
    public ResponseEntity<List<SeatDto>> getAllSeats() {
        return ResponseEntity.ok(seatService.getAllSeats());
    }

    @GetMapping("/screen/{screenId}")
    public ResponseEntity<List<SeatDto>> getSeatsByScreenId(@PathVariable Long screenId) {
        return ResponseEntity.ok(seatService.getSeatsByScreenId(screenId));
    }

    @GetMapping("/screen/{screenId}/type/{seatType}")
    public ResponseEntity<List<SeatDto>> getSeatsByScreenIdAndType(@PathVariable Long screenId, @PathVariable String seatType) {
        return ResponseEntity.ok(seatService.getSeatsByScreenIdAndType(screenId, seatType));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeatDto> updateSeat(@PathVariable Long id, @Valid @RequestBody SeatDto seatDto) {
        return ResponseEntity.ok(seatService.updateSeat(id, seatDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id) {
        seatService.deleteSeat(id);
        return ResponseEntity.noContent().build();
    }
}
