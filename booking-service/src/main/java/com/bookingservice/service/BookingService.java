package com.bookingservice.service;

import com.bookingservice.dtos.BookingCreateDTO;
import com.bookingservice.dtos.BookingDTO;
import com.bookingservice.dtos.BookingResponseDTO;

import java.util.List;

public interface BookingService {

    BookingResponseDTO createBooking(BookingCreateDTO bookingDto);

    BookingResponseDTO getBookingByReference(String reference);

    List<BookingDTO> getBookingsByUser(Long userId);

    BookingResponseDTO confirmBooking(String reference);

    BookingResponseDTO cancelBooking(String reference, String reason);

    void processPaymentComplete(String bookingReference, String paymentId);

    void processExpiredBookings();

    List<Long> getBookedSeatsByShow(Long showId);
}
