package com.bookingservice.repositories;

import com.bookingservice.entities.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingSeatRepository extends JpaRepository<BookingSeat,Long> {

    List<BookingSeat> findByBookingId(Long bookingId);

    @Query("SELECT bs.seatId FROM BookingSeat bs JOIN bs.booking b WHERE b.showId = :showId AND b.status IN ('CONFIRMED', 'PAID')")
    List<Long> findBookedSeatIdsByShowId(Long showId);
}
