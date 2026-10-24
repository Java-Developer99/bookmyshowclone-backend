package com.movie_event_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowDto {

    private Long id;

    @NotNull(message = "Movie ID cannot be null")
    private Long movieId;

    @NotNull(message = "Start time cannot be null")
    private LocalDateTime startTime;

    @NotNull(message = "End time cannot be null")
    private LocalDateTime endTime;

    @NotNull(message = "Theater ID cannot be null")
    private Long theaterId;

    @NotNull(message = "Screen ID cannot be null")
    private Long screenId;

    private Boolean active;
}
