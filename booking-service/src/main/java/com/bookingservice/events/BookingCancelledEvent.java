package com.bookingservice.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingCancelledEvent {

    private String bookingReference;
    private Long showId;
    private Long theaterId;
    private List<Long> seatIds;
    private String reason;
    private LocalDateTime canceledAt;
}
