package com.theater_venue_service.repositories;

import com.theater_venue_service.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat,Long> {

    List<Seat> findByScreenId(Long screenId);
    List<Seat> findByScreenIdAndSeatType(Long screenId,String seatType);

}
