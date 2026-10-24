package com.movie_event_service.services.impl;

import com.movie_event_service.entity.Genre;
import com.movie_event_service.exceptions.BadRequestException;
import com.movie_event_service.exceptions.ResourceNotFoundException;
import com.movie_event_service.repository.GenreRepository;
import com.movie_event_service.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {

    @Autowired
    private GenreRepository genreRepository;


    @Override
    @Transactional
    public Genre createGenre(Genre genre) {
        if(genreRepository.existsByName(genre.getName())){
            throw new BadRequestException("Genre with name '"+ genre.getName()+ "'already exists");
        }
        return genreRepository.save(genre);

    }

    @Override
    @Transactional(readOnly = true)
    public Genre getGenreById(Long id) {

        return genreRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Genre","id",id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @Override
    @Transactional
    public Genre updateGenre(Long id, Genre genreDetails) {

        Genre genre=genreRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Genre","id",id));

        if(!genre.getName().equals(genreDetails.getName())&&
           genreRepository.existsByName(genreDetails.getName())){
            throw new BadRequestException("Genre with name '"+ genreDetails.getName() + "' already exists");
        }

        genre.setName(genreDetails.getName());
        genre.setDescription(genreDetails.getDescription());

        return genreRepository.save(genre);

    }

    @Override
    @Transactional
    public void deleteGenre(Long id) {

        Genre genre=genreRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Genre","id",id));

        genreRepository.delete(genre);
    }
}
