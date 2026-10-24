package com.movie_event_service.controllers;

import com.movie_event_service.dto.MovieDto;
import com.movie_event_service.dto.MovieResponseDto;
import com.movie_event_service.dto.PagedResponseDto;
import com.movie_event_service.services.MovieService;
import com.movie_event_service.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@Tag(name = "Movie Controller",description = "APIs for movie operations")
@CrossOrigin
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping
    @Operation(summary = "Create a new movie")
    public ResponseEntity<MovieResponseDto> createMovie(@Valid @RequestBody MovieDto movieDto){
        return new ResponseEntity<>(movieService.createMovie(movieDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get movie by ID")
    public ResponseEntity<MovieResponseDto> getMovieById(@PathVariable Long id){
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @GetMapping
    @Operation(summary = "Get All Movies with Pagination ")
    public ResponseEntity<PagedResponseDto<MovieResponseDto>> getAllMovies(
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size){
        return ResponseEntity.ok(movieService.getAllMovies(page,size));
    }

    @GetMapping("/search")
    @Operation(summary = "Search movies by title,genre, and/or language")
    public ResponseEntity<PagedResponseDto<MovieResponseDto>> searchMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long genreId,
            @RequestParam(required = false) String language,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size){
        return ResponseEntity.ok(movieService.searchMovies(title,genreId,language,page,size));
    }

    @PutMapping("{id}")
    @Operation(summary = "Update movie")
    public ResponseEntity<MovieResponseDto> updateMovie(
            @PathVariable Long id,
            @Valid @RequestBody MovieDto movieDto){
        return ResponseEntity.ok(movieService.updateMovie(id,movieDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete movie (soft delete)")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id){
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/upcoming")
    @Operation(summary = "Get upcoming movies")
    public ResponseEntity<List<MovieResponseDto>> getUpcomingMovies(){
        return ResponseEntity.ok(movieService.getUpcomingMovies());
    }

    @GetMapping("/top-rated")
    @Operation(summary = "Get top rated movies")
    public ResponseEntity<List<MovieResponseDto>> getTopRatedMovies(){
        return ResponseEntity.ok(movieService.getTopRatedMovies());
    }


}
