package com.movie_event_service.services;

import com.movie_event_service.dto.PagedResponseDto;
import com.movie_event_service.dto.ShowDto;

import java.time.LocalDate;
import java.util.List;

public interface ShowService {

    ShowDto createShow(ShowDto showDTO);
    ShowDto getShowById(Long id);
    PagedResponseDto<ShowDto> getAllShows(int page, int size);
    List<ShowDto> getShowsByMovie(Long movieId);
    List<ShowDto> getShowsByMovieTheaterAndDate(Long movieId, Long theaterId, LocalDate date);
    List<ShowDto> getShowsByTheaterAndDate(Long theaterId, LocalDate date);
    ShowDto updateShow(Long id, ShowDto showDTO);
    void deleteShow(Long id);
}
