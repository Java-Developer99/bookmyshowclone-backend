package com.bookingservice.events;

import com.bookingservice.dtos.BookingDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreatedEvent {

    private String bookingReference;
    private Long userId;
    private Long showId;
    private Long theaterId;
    private LocalDateTime createdAt;
    private LocalDateTime expiryTime;
    private BookingDTO booking;
}
