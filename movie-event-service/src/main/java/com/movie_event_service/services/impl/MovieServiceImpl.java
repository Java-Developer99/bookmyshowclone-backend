package com.movie_event_service.services.impl;

import com.movie_event_service.dto.MovieDto;
import com.movie_event_service.dto.MovieResponseDto;
import com.movie_event_service.dto.PagedResponseDto;
import com.movie_event_service.entity.Genre;
import com.movie_event_service.entity.Movie;
import com.movie_event_service.enums.Language;
import com.movie_event_service.exceptions.BadRequestException;
import com.movie_event_service.exceptions.ResourceNotFoundException;
import com.movie_event_service.repository.GenreRepository;
import com.movie_event_service.repository.MovieRepository;
import com.movie_event_service.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Override
    @Transactional
    public MovieResponseDto createMovie(MovieDto movieDTO) {

        Set<Genre> genres=new HashSet<>();
        if (movieDTO.getGenreIds() !=null && !movieDTO.getGenreIds().isEmpty()){
            movieDTO.getGenreIds().forEach(genreId->{
                Genre genre=genreRepository.findById(genreId)
                        .orElseThrow(()->new ResourceNotFoundException("Genre", "id",genreId));
                genres.add(genre);
            });
        }

        Movie movie = Movie.builder()
                .title(movieDTO.getTitle())
                .description(movieDTO.getDescription())
                .durationMinutes(movieDTO.getDurationMinutes())
                .genres(genres)
                .language(movieDTO.getLanguage())
                .releaseDate(movieDTO.getReleaseDate())
                .posterUrl(movieDTO.getPosterUrl())
                .trailerUrl(movieDTO.getTrailerUrl())
                .active(movieDTO.getActive() != null ? movieDTO.getActive() : true)
                .build();

        Movie savedMovie = movieRepository.save(movie);
        return mapToMovieResponseDTO(savedMovie);

    }

    @Override
    @Transactional(readOnly = true)
    public MovieResponseDto getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        return mapToMovieResponseDTO(movie);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<MovieResponseDto> getAllMovies(int page, int size) {
        validatePageNumberAndSize(page,size);

        Pageable pageable= PageRequest.of(page,size, Sort.by("releaseDate").descending());
        Page<Movie> movies=movieRepository.findByActiveTrue(pageable);

        List<MovieResponseDto> content=movies.getContent().stream()
                .map(this::mapToMovieResponseDTO)
                .collect(Collectors.toList());

        return new PagedResponseDto<>(
                content,
                movies.getNumber(),
                movies.getSize(),
                movies.getTotalElements(),
                movies.getTotalPages(),
                movies.isLast()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<MovieResponseDto> searchMovies(String title, Long genreId, String language, int page, int size) {
        validatePageNumberAndSize(page, size);

        Language languageEnum = null;
        if (language != null) {
            try {
                languageEnum = Language.valueOf(language.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid language: " + language);
            }
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Movie> movies = movieRepository.searchMovies(title, genreId, language, pageable);

        List<MovieResponseDto> content = movies.getContent().stream()
                .map(this::mapToMovieResponseDTO)
                .collect(Collectors.toList());

        return new PagedResponseDto<>(
                content,
                movies.getNumber(),
                movies.getSize(),
                movies.getTotalElements(),
                movies.getTotalPages(),
                movies.isLast()
        );
    }

    @Override
    @Transactional
    public MovieResponseDto updateMovie(Long id, MovieDto movieDTO) {
        Movie movie=movieRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Movie","id",id));

        movie.setTitle(movieDTO.getTitle());
        movie.setDescription(movieDTO.getDescription());
        movie.setDurationMinutes(movieDTO.getDurationMinutes());
        movie.setLanguage(movieDTO.getLanguage());
        movie.setReleaseDate(movieDTO.getReleaseDate());
        movie.setPosterUrl(movieDTO.getPosterUrl());
        movie.setTrailerUrl(movieDTO.getTrailerUrl());

        if(movieDTO.getActive() !=null){
            movie.setActive(movieDTO.getActive());
        }

        if(movieDTO.getGenreIds() !=null){
            Set<Genre> genres=new HashSet<>();
            movieDTO.getGenreIds().forEach(genreId->{
                Genre genre=genreRepository.findById(genreId)
                        .orElseThrow(()->new ResourceNotFoundException("Genre","id",genreId));
                genres.add(genre);
            });
            movie.setGenres(genres);
        }
        Movie updatedMovie=movieRepository.save(movie);
        return mapToMovieResponseDTO(updatedMovie);
    }

    @Override
    @Transactional
    public void deleteMovie(Long id) {

        Movie movie=movieRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Movie","id",id));

        movie.setActive(false);
        movieRepository.save(movie);

    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieResponseDto> getUpcomingMovies() {
        LocalDate today=LocalDate.now();
        List<Movie> upcomingMovies=movieRepository.findByReleaseDateGreaterThanEqualAndActiveTrue(today);

        return upcomingMovies.stream()
                .map(this::mapToMovieResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieResponseDto> getTopRatedMovies() {
        List<Movie> topRatedMovies=movieRepository.findTop10ByActiveTrueOrderByRatingDesc();

        return topRatedMovies.stream()
                .map(this::mapToMovieResponseDTO)
                .collect(Collectors.toList());
    }

    private MovieResponseDto mapToMovieResponseDTO(Movie movie) {
        return MovieResponseDto.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .durationMinutes(movie.getDurationMinutes())
                .genres(movie.getGenres())
                .language(movie.getLanguage())
                .releaseDate(movie.getReleaseDate())
                .posterUrl(movie.getPosterUrl())
                .trailerUrl(movie.getTrailerUrl())
                .rating(movie.getRating())
                .active(movie.isActive())
                .build();
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size < 1 || size > 100) {
            throw new BadRequestException("Size must not be less than one.");
        }
    }
}
