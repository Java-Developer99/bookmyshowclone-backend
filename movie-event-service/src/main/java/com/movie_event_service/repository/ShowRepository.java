package com.movie_event_service.repository;

import com.movie_event_service.entity.Show;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show,Long> {

    Page<Show> findByActiveTrue(Pageable pageable);

    List<Show> findByMovieIdAndActiveTrueOrderByStartTime(Long movieId);

    @Query("SELECT s FROM Show s WHERE s.active = true AND s.movie.id = :movieId AND " +
            "s.startTime >= :startDate AND s.startTime < :endDate AND s.theaterId = :theaterId")
    List<Show> findShowsByMovieTheaterAndDate(
            @Param("movieId") Long movieId,
            @Param("theaterId") Long theaterId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    List<Show> findByTheaterIdAndStartTimeGreaterThanEqualAndStartTimeLessThanAndActiveTrue(
            Long theaterId, LocalDateTime startDate, LocalDateTime endDate);

}
