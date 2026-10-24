package com.bookingservice.dtos;

import com.bookingservice.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookingResponseDTO {

    private String bookingReference;
    private BookingStatus status;
    private LocalDateTime expiryTime;
    private LocalDateTime showTime;
    private Long theaterId;
    private String theaterName; // This can be fetched from Theater service
    private String movieTitle;  // This can be fetched from Movie service
    private Integer ticketCount;
    private BigDecimal totalPrice;
    private List<SeatBookingDTO> seats;
    private String message;
}
