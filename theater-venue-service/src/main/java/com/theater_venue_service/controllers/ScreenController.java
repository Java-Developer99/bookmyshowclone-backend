package com.theater_venue_service.controllers;

import com.theater_venue_service.dtos.ScreenDto;
import com.theater_venue_service.services.ScreenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/screens")
@CrossOrigin
public class ScreenController {

    @Autowired
    private ScreenService screenService;


    @PostMapping
    public ResponseEntity<ScreenDto> createScreen(@Valid @RequestBody ScreenDto screenDto) {
        return new ResponseEntity<>(screenService.createScreen(screenDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScreenDto> getScreenById(@PathVariable Long id) {
        return ResponseEntity.ok(screenService.getScreenById(id));
    }

    @GetMapping
    public ResponseEntity<List<ScreenDto>> getAllScreens() {
        return ResponseEntity.ok(screenService.getAllScreens());
    }

    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<ScreenDto>> getScreensByTheaterId(@PathVariable Long theaterId) {
        return ResponseEntity.ok(screenService.getScreensByTheaterId(theaterId));
    }

    @GetMapping("/type/{screenType}")
    public ResponseEntity<List<ScreenDto>> getScreensByType(@PathVariable String screenType) {
        return ResponseEntity.ok(screenService.getScreensByType(screenType));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScreenDto> updateScreen(@PathVariable Long id, @Valid @RequestBody ScreenDto screenDto) {
        return ResponseEntity.ok(screenService.updateScreen(id, screenDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScreen(@PathVariable Long id) {
        screenService.deleteScreen(id);
        return ResponseEntity.noContent().build();
    }
}
