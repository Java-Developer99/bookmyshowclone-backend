package com.bookingservice.controller;

import com.bookingservice.dtos.BookingCreateDTO;
import com.bookingservice.dtos.BookingDTO;
import com.bookingservice.dtos.BookingResponseDTO;
import com.bookingservice.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/bookings")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin
public class BookingController {

    private final BookingService bookingService;


    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(@Valid @RequestParam BookingCreateDTO bookingCreateDTO){
        log.info("Rest request to create booking: {}",bookingCreateDTO);
        BookingResponseDTO response=bookingService.createBooking(bookingCreateDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{reference}")
    public ResponseEntity<BookingResponseDTO> getBookingByReference(@PathVariable String reference) {
        log.info("REST request to get booking by reference: {}", reference);
        BookingResponseDTO response = bookingService.getBookingByReference(reference);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingDTO>> getBookingsByUser(@PathVariable Long userId) {
        log.info("REST request to get bookings for user: {}", userId);
        List<BookingDTO> bookings = bookingService.getBookingsByUser(userId);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/{reference}/confirm")
    public ResponseEntity<BookingResponseDTO> confirmBooking(@PathVariable String reference) {
        log.info("REST request to confirm booking: {}", reference);
        BookingResponseDTO response = bookingService.confirmBooking(reference);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{reference}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(
            @PathVariable String reference,
            @RequestParam(required = false, defaultValue = "Canceled by user") String reason) {
        log.info("REST request to cancel booking: {}, reason: {}", reference, reason);
        BookingResponseDTO response = bookingService.cancelBooking(reference, reason);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/show/{showId}/seats")
    public ResponseEntity<Map<String, List<Long>>> getBookedSeatsByShow(@PathVariable Long showId) {
        log.info("REST request to get booked seats for show: {}", showId);
        List<Long> bookedSeats = bookingService.getBookedSeatsByShow(showId);
        Map<String, List<Long>> response = new HashMap<>();
        response.put("bookedSeats", bookedSeats);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{reference}/payment-complete")
    public ResponseEntity<Void> processPaymentComplete(
            @PathVariable String reference,
            @RequestParam String paymentId) {
        log.info("REST request to process payment complete for booking: {}, payment: {}", reference, paymentId);
        bookingService.processPaymentComplete(reference, paymentId);
        return ResponseEntity.ok().build();
    }

}
