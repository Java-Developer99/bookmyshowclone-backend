package com.movie_event_service.services.impl;

import com.movie_event_service.dto.PagedResponseDto;
import com.movie_event_service.dto.ShowDto;
import com.movie_event_service.entity.Movie;
import com.movie_event_service.entity.Show;
import com.movie_event_service.exceptions.BadRequestException;
import com.movie_event_service.exceptions.ResourceNotFoundException;
import com.movie_event_service.repository.MovieRepository;
import com.movie_event_service.repository.ShowRepository;
import com.movie_event_service.services.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowServiceImpl implements ShowService {


    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Override
    @Transactional
    public ShowDto createShow(ShowDto showDTO) {
        Movie movie=movieRepository.findById(showDTO.getMovieId())
                .orElseThrow(()->new ResourceNotFoundException("Movie","id",showDTO.getMovieId()));

        if(showDTO.getStartTime().isAfter(showDTO.getEndTime())){
            throw new BadRequestException("Show start time must be before end time");
        }

        Show show=Show.builder()
                .movie(movie)
                .startTime(showDTO.getStartTime())
                .endTime(showDTO.getEndTime())
                .theaterId(showDTO.getTheaterId())
                .screenId(showDTO.getScreenId())
                .active(showDTO.getActive()!= null ? showDTO.getActive():true)
                .build();

        Show savedShow= showRepository.save(show);

        return mapToShowDTO(savedShow);
    }

    @Override
    @Transactional(readOnly = true)
    public ShowDto getShowById(Long id) {

        Show show=showRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Show","id",id));

        return mapToShowDTO(show);


    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<ShowDto> getAllShows(int page, int size) {
        validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").ascending());
        Page<Show> shows = showRepository.findByActiveTrue(pageable);

        List<ShowDto> content = shows.getContent().stream()
                .map(this::mapToShowDTO)
                .collect(Collectors.toList());

        return new PagedResponseDto<>(
                content,
                shows.getNumber(),
                shows.getSize(),
                shows.getTotalElements(),
                shows.getTotalPages(),
                shows.isLast()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShowDto> getShowsByMovie(Long movieId) {

        if(!movieRepository.existsById(movieId)){
            throw new ResourceNotFoundException("Movie","id",movieId);
        }

        List<Show> shows=showRepository.findByMovieIdAndActiveTrueOrderByStartTime(movieId);

        return shows.stream()
                .map(this::mapToShowDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShowDto> getShowsByMovieTheaterAndDate(Long movieId, Long theaterId, LocalDate date) {
        if(!movieRepository.existsById(movieId)){
            throw new ResourceNotFoundException("Movie","id",movieId);
        }

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<Show> shows=showRepository.findShowsByMovieTheaterAndDate(
                movieId,theaterId,startOfDay,endOfDay);

        return shows.stream()
                .map(this::mapToShowDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShowDto> getShowsByTheaterAndDate(Long theaterId, LocalDate date) {
        LocalDateTime startOfDay=date.atStartOfDay();
        LocalDateTime endOfDay=date.atTime(LocalTime.MAX);

        List<Show> shows=showRepository.findByTheaterIdAndStartTimeGreaterThanEqualAndStartTimeLessThanAndActiveTrue(
                theaterId,startOfDay,endOfDay);

        return shows.stream()
                .map(this::mapToShowDTO)
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public ShowDto updateShow(Long id, ShowDto showDTO) {

        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show", "id", id));


        if (showDTO.getMovieId() != null && !showDTO.getMovieId().equals(show.getMovie().getId())) {
            Movie movie = movieRepository.findById(showDTO.getMovieId())
                    .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", showDTO.getMovieId()));
            show.setMovie(movie);
        }

        if (showDTO.getStartTime() != null && showDTO.getEndTime() != null) {
            if (showDTO.getStartTime().isAfter(showDTO.getEndTime())) {
                throw new BadRequestException("Show start time must be before end time");
            }
            show.setStartTime(showDTO.getStartTime());
            show.setEndTime(showDTO.getEndTime());
        } else if (showDTO.getStartTime() != null) {
            show.setStartTime(showDTO.getStartTime());
        } else if (showDTO.getEndTime() != null) {
            show.setEndTime(showDTO.getEndTime());
        }

        if (showDTO.getTheaterId() != null) {
            show.setTheaterId(showDTO.getTheaterId());
        }

        if (showDTO.getScreenId() != null) {
            show.setScreenId(showDTO.getScreenId());
        }

        if (showDTO.getActive() != null) {
            show.setActive(showDTO.getActive());
        }


        Show updatedShow = showRepository.save(show);

        return mapToShowDTO(updatedShow);
    }

    @Override
    @Transactional
    public void deleteShow(Long id) {

        Show show=showRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Show","id",id));

        show.setActive(false);
        showRepository.save(show);

    }

    private ShowDto mapToShowDTO(Show show){
        return ShowDto.builder()
                .id(show.getId())
                .movieId(show.getMovie().getId())
                .startTime(show.getStartTime())
                .endTime(show.getEndTime())
                .theaterId(show.getTheaterId())
                .screenId(show.getScreenId())
                .active(show.isActive())
                .build();
    }

    private void validatePageNumberAndSize(int page,int size){
        if(page<0){
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if(size < 1 || size > 100){
            throw new BadRequestException("Page size must be between 1 to 100");
        }
    }
}
