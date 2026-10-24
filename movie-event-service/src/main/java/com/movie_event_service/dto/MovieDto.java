package com.movie_event_service.dto;

import com.movie_event_service.enums.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class MovieDto {

    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @NotNull(message = "Duration cannot be null")
    private Integer durationMinutes;

    private Set<Long> genreIds;

    @NotNull(message = "Language cannot be null")
    private Language language;

    @NotNull(message = "Release date cannot be null")
    private LocalDate releaseDate;

    private String posterUrl;

    private String trailerUrl;

    private Boolean active;
}
