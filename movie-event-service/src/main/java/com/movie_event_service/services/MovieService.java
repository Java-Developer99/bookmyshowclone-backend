package com.movie_event_service.services;

import com.movie_event_service.dto.MovieDto;
import com.movie_event_service.dto.MovieResponseDto;
import com.movie_event_service.dto.PagedResponseDto;

import java.util.List;

public interface MovieService {

    MovieResponseDto createMovie(MovieDto movieDTO);

    MovieResponseDto getMovieById(Long id);

    PagedResponseDto<MovieResponseDto> getAllMovies(int page, int size);

    PagedResponseDto<MovieResponseDto> searchMovies(String title, Long genreId, String language, int page, int size);

    MovieResponseDto updateMovie(Long id, MovieDto movieDTO);

    void deleteMovie(Long id);

    List<MovieResponseDto> getUpcomingMovies();

    List<MovieResponseDto> getTopRatedMovies();
}
