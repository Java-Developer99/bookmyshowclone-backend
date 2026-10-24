package com.bookingservice.utils;

import com.bookingservice.dtos.SeatBookingDTO;
import com.bookingservice.entities.BookingSeat;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BookingUtils {

    private BookingUtils(){

    }

    //generate booking reference using alpha_numeric value
    public static String generateBookingReference(){
        final String ALPHA_NUMERIC="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder builder=new StringBuilder("Book-");

        for(int i=0;i<5;i++){
            int index= ThreadLocalRandom.current().nextInt(ALPHA_NUMERIC.length());
            builder.append(ALPHA_NUMERIC.charAt(index));
        }
        return builder.toString();
    }

    //generates total calculated price of ticket
    public static BigDecimal calculateTotalPrice(List<SeatBookingDTO> seats) {
        return seats.stream()
                .map(SeatBookingDTO::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    //calculates total price of seat entity
    public static BigDecimal calculateTotalPriceFromEntities(List<BookingSeat> seats) {
        return seats.stream()
                .map(BookingSeat::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


}
