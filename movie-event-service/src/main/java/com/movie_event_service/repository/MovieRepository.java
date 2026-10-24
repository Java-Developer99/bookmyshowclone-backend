package com.movie_event_service.repository;

import com.movie_event_service.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {

    Page<Movie> findByActiveTrue(Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.active = true AND " +
            "(:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:genreId IS NULL OR EXISTS (SELECT 1 FROM m.genres g WHERE g.id = :genreId)) AND " +
            "(:language IS NULL OR m.language = :language)")
    Page<Movie> searchMovies(
            @Param("title") String title,
            @Param("genreId") Long genreId,
            @Param("language") String language,
            Pageable pageable
    );

    List<Movie> findByReleaseDateGreaterThanEqualAndActiveTrue(LocalDate date);

    List<Movie> findTop10ByActiveTrueOrderByRatingDesc();
}
