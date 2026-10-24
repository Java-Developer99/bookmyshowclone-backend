package com.movie_event_service.controllers;

import com.movie_event_service.dto.PagedResponseDto;
import com.movie_event_service.dto.ShowDto;
import com.movie_event_service.services.ShowService;
import com.movie_event_service.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/shows")
@Tag(name = "Show Controller", description = "APIs for show operations")
@CrossOrigin
public class ShowController {

    @Autowired
    private ShowService showService;

    @PostMapping
    @Operation(summary = "Create a new show ")
    public ResponseEntity<ShowDto> createShow(@Valid @RequestBody ShowDto showDto){
        return new ResponseEntity<>(showService.createShow(showDto), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get show by ID")
    public ResponseEntity<ShowDto> getShowById(@PathVariable Long id){
        return ResponseEntity.ok(showService.getShowById(id));
    }

    @GetMapping
    @Operation(summary = "Get all shows with pagination")
    public ResponseEntity<PagedResponseDto<ShowDto>> getAllShows(
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size)
    {
        return ResponseEntity.ok(showService.getAllShows(page,size));
    }

    @GetMapping("/movie/{movieId}")
    @Operation(summary = "Get shows by movie")
    public ResponseEntity<List<ShowDto>> getShowsByMovie(@PathVariable Long movieId){
        return ResponseEntity.ok(showService.getShowsByMovie(movieId));
    }

    @GetMapping("/movie/{movieId}/theater/{theaterId}")
    @Operation(summary = "Get shows by movie, theater and date")
    public ResponseEntity<List<ShowDto>> getShowsByMovieTheaterAndDate(
            @PathVariable Long movieId,
            @PathVariable Long theaterId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return ResponseEntity.ok(showService.getShowsByMovieTheaterAndDate(movieId,theaterId,date));
    }

    @GetMapping("/theater/{theaterId}/date")
    @Operation(summary = "Get shows by theater and date")
    public ResponseEntity<List<ShowDto>> getShowsByTheaterAndDate(
            @PathVariable Long theaterId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(showService.getShowsByTheaterAndDate(theaterId, date));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update show")
    public ResponseEntity<ShowDto> updateShow(
            @PathVariable Long id,
            @Valid @RequestBody ShowDto showDTO) {
        return ResponseEntity.ok(showService.updateShow(id, showDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete show (soft delete)")
    public ResponseEntity<Void> deleteShow(@PathVariable Long id) {
        showService.deleteShow(id);
        return ResponseEntity.noContent().build();
    }

}
