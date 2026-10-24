package com.bookingservice.repositories;

import com.bookingservice.entities.Booking;
import com.bookingservice.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

    Optional<Booking> findByBookingReference(String bookingReference);

    List<Booking> findByUserId(Long userId);

    List<Booking> findByShowIdAndStatus(Long showId, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.status = :status AND b.expiryTime < :currentTime")
    List<Booking> findExpiredBookings(BookingStatus status, LocalDateTime currentTime);

    @Query("SELECT b FROM Booking b WHERE b.showId = :showId AND b.status IN ('CONFIRMED', 'PAID')")
    List<Booking> findConfirmedBookingsByShowId(Long showId);

    List<Booking> findByUserIdAndStatus(Long userId, BookingStatus status);
}
