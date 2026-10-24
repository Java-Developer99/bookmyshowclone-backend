package com.theater_venue_service.repositories;

import com.theater_venue_service.entity.SeatLayout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatLayoutRepository extends JpaRepository<SeatLayout,Long> {


    Optional<SeatLayout> findByScreenId(Long screenId);
}
