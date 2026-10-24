package com.bookingservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatBookingDTO {

    @NotNull(message = "Seat ID is required")
    private Long seatId;

    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    @NotBlank(message = "Row name is required")
    private String rowName;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
}
