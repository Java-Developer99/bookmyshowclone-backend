package com.movie_event_service.services;

import com.movie_event_service.entity.Genre;

import java.util.List;

public interface GenreService {

    Genre createGenre(Genre genre);
    Genre getGenreById(Long id);
    List<Genre> getAllGenres();
    Genre updateGenre(Long id, Genre genre);
    void deleteGenre(Long id);
}
