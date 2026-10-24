package com.bookingservice.dtos;

import com.bookingservice.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDTO {

    private Long id;
    private String bookingReference;
    private Long userId;
    private Long showId;
    private Long theaterId;
    private LocalDateTime showTime;
    private Integer ticketCount;
    private BigDecimal totalPrice;
    private BookingStatus status;
    private LocalDateTime expiryTime;
    private List<SeatBookingDTO> seats;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
