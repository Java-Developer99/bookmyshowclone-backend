package com.movie_event_service.dto;

import com.movie_event_service.entity.Genre;
import com.movie_event_service.enums.Language;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieResponseDto {

    private Long id;
    private String title;
    private String description;
    private Integer durationMinutes;
    private Set<Genre> genres;
    private Language language;
    private LocalDate releaseDate;
    private String posterUrl;
    private String trailerUrl;
    private Double rating;
    private boolean active;
}
