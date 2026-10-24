package com.bookingservice.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingCompletedEvent {

    private String bookingReference;
    private Long userId;
    private Long showId;
    private Long theaterId;
    private List<Long> seatIds;
    private String paymentId;
    private LocalDateTime completedAt;
}
