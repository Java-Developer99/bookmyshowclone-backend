package com.bookingservice.service.impl;

import com.bookingservice.dtos.BookingCreateDTO;
import com.bookingservice.dtos.BookingDTO;
import com.bookingservice.dtos.BookingResponseDTO;
import com.bookingservice.dtos.SeatBookingDTO;
import com.bookingservice.entities.Booking;
import com.bookingservice.entities.BookingSeat;
import com.bookingservice.enums.BookingStatus;
import com.bookingservice.events.BookingCancelledEvent;
import com.bookingservice.events.BookingCompletedEvent;
import com.bookingservice.events.BookingCreatedEvent;
import com.bookingservice.exceptions.BookingException;
import com.bookingservice.exceptions.BookingNotFoundException;
import com.bookingservice.exceptions.SeatUnavailableException;
import com.bookingservice.repositories.BookingRepository;
import com.bookingservice.repositories.BookingSeatRepository;
import com.bookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingSeatRepository bookingSeatRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private final KafkaTemplate<String,Object> kafkaTemplate;

    @Value("${booking.expiry.minutes:15}")
    private int bookingExpiryMinutes;

    @Value("${kafka.topics.booking-created}")
    private String bookingCreatedTopic;

    @Value("${kafka.topics.booking-completed}")
    private String bookingCompletedTopic;

    @Value("${kafka.topics.booking-canceled}")
    private String bookingCanceledTopic;

    @Override
    @Transactional
    public BookingResponseDTO createBooking(BookingCreateDTO bookingDto) {
        log.info("Creating new booking for user: {},show: {}",bookingDto.getUserId(),bookingDto.getShowId());

        List<Long> selectedSeatIds = bookingDto.getSeats().stream()
                .map(SeatBookingDTO::getSeatId)
                .collect(Collectors.toList());

        List<Long> bookedSeatIds = bookingSeatRepository.findBookedSeatIdsByShowId(bookingDto.getShowId());

        List<Long> unavailableSeats = selectedSeatIds.stream()
                .filter(bookedSeatIds::contains)
                .collect(Collectors.toList());

        if (!unavailableSeats.isEmpty()) {
            String message = "Seats are no longer available: " + unavailableSeats;
            log.error(message);
            throw new SeatUnavailableException(message);
        }

        BigDecimal totalPrice = bookingDto.getSeats().stream()
                .map(SeatBookingDTO::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Booking booking = Booking.builder()
                .userId(bookingDto.getUserId())
                .showId(bookingDto.getShowId())
                .theaterId(bookingDto.getTheaterId())
                .showTime(LocalDateTime.now()) // Replace with actual show time from show service
                .ticketCount(bookingDto.getSeats().size())
                .totalPrice(totalPrice)
                .status(BookingStatus.PENDING)
                .expiryTime(LocalDateTime.now().plusMinutes(bookingExpiryMinutes))
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        List<BookingSeat> bookingSeats = new ArrayList<>();
        for (SeatBookingDTO seatDto : bookingDto.getSeats()) {
            BookingSeat bookingSeat = BookingSeat.builder()
                    .booking(savedBooking)
                    .seatId(seatDto.getSeatId())
                    .seatNumber(seatDto.getSeatNumber())
                    .rowName(seatDto.getRowName())
                    .price(seatDto.getPrice())
                    .build();
            bookingSeats.add(bookingSeat);
        }

        bookingSeatRepository.saveAll(bookingSeats);

        BookingCreatedEvent event = BookingCreatedEvent.builder()
                .bookingReference(savedBooking.getBookingReference())
                .userId(savedBooking.getUserId())
                .showId(savedBooking.getShowId())
                .theaterId(savedBooking.getTheaterId())
                .createdAt(savedBooking.getCreatedAt())
                .expiryTime(savedBooking.getExpiryTime())
                .booking(mapToBookingDTO(savedBooking, bookingDto.getSeats()))
                .build();

        kafkaTemplate.send(bookingCreatedTopic,savedBooking.getBookingReference(),event);
        log.info("Published booking created event: {}",savedBooking.getBookingReference());

        return BookingResponseDTO.builder()
                .bookingReference(savedBooking.getBookingReference())
                .status(savedBooking.getStatus())
                .expiryTime(savedBooking.getExpiryTime())
                .showTime(savedBooking.getShowTime())
                .theaterId(savedBooking.getTheaterId())
                .ticketCount(savedBooking.getTicketCount())
                .totalPrice(savedBooking.getTotalPrice())
                .seats(bookingDto.getSeats())
                .message("Booking created successfully. Please complete payment within " + bookingExpiryMinutes + " minutes.")
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDTO getBookingByReference(String reference) {
        log.info("Fetching booking with reference: {}", reference);

        Booking booking = bookingRepository.findByBookingReference(reference)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with reference: " + reference));

        List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingId(booking.getId());
        List<SeatBookingDTO> seatDtos = bookingSeats.stream()
                .map(this::mapToSeatBookingDTO)
                .collect(Collectors.toList());

        return BookingResponseDTO.builder()
                .bookingReference(booking.getBookingReference())
                .status(booking.getStatus())
                .expiryTime(booking.getExpiryTime())
                .showTime(booking.getShowTime())
                .theaterId(booking.getTheaterId())
                .ticketCount(booking.getTicketCount())
                .totalPrice(booking.getTotalPrice())
                .seats(seatDtos)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDTO> getBookingsByUser(Long userId) {
        log.info("Fetching bookings for user: {}", userId);

        List<Booking> bookings = bookingRepository.findByUserId(userId);

        return bookings.stream()
                .map(booking -> {
                    List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingId(booking.getId());
                    List<SeatBookingDTO> seatDtos = bookingSeats.stream()
                            .map(this::mapToSeatBookingDTO)
                            .collect(Collectors.toList());
                    return mapToBookingDTO(booking, seatDtos);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingResponseDTO confirmBooking(String reference) {
        log.info("Confirming booking with reference: {}", reference);

        Booking booking = bookingRepository.findByBookingReference(reference)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with reference: " + reference));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BookingException("Booking cannot be confirmed. Current status: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setExpiryTime(LocalDateTime.now().plusMinutes(bookingExpiryMinutes));

        bookingRepository.save(booking);

        List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingId(booking.getId());
        List<SeatBookingDTO> seatDtos = bookingSeats.stream()
                .map(this::mapToSeatBookingDTO)
                .collect(Collectors.toList());

        return BookingResponseDTO.builder()
                .bookingReference(booking.getBookingReference())
                .status(booking.getStatus())
                .expiryTime(booking.getExpiryTime())
                .showTime(booking.getShowTime())
                .theaterId(booking.getTheaterId())
                .ticketCount(booking.getTicketCount())
                .totalPrice(booking.getTotalPrice())
                .seats(seatDtos)
                .message("Booking confirmed successfully. Please complete payment within " + bookingExpiryMinutes + " minutes.")
                .build();
    }

    @Override
    @Transactional
    public BookingResponseDTO cancelBooking(String reference, String reason) {
        log.info("Canceling booking with reference: {}", reference);

        Booking booking = bookingRepository.findByBookingReference(reference)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with reference: " + reference));

        if (booking.getStatus() == BookingStatus.PAID || booking.getStatus() == BookingStatus.CANCELED) {
            throw new BookingException("Booking cannot be canceled. Current status: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.CANCELED);
        bookingRepository.save(booking);

        List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingId(booking.getId());
        List<SeatBookingDTO> seatDtos = bookingSeats.stream()
                .map(this::mapToSeatBookingDTO)
                .collect(Collectors.toList());

        List<Long> seatIds = bookingSeats.stream()
                .map(BookingSeat::getSeatId)
                .collect(Collectors.toList());

        // Publish booking canceled event
        BookingCancelledEvent event = BookingCancelledEvent.builder()
                .bookingReference(booking.getBookingReference())
                .showId(booking.getShowId())
                .theaterId(booking.getTheaterId())
                .seatIds(seatIds)
                .reason(reason)
                .canceledAt(LocalDateTime.now())
                .build();

        kafkaTemplate.send(bookingCanceledTopic, booking.getBookingReference(), event);
        log.info("Published booking canceled event: {}", booking.getBookingReference());

        return BookingResponseDTO.builder()
                .bookingReference(booking.getBookingReference())
                .status(booking.getStatus())
                .showTime(booking.getShowTime())
                .theaterId(booking.getTheaterId())
                .ticketCount(booking.getTicketCount())
                .totalPrice(booking.getTotalPrice())
                .seats(seatDtos)
                .message("Booking canceled successfully.")
                .build();
    }

    @Override
    public void processPaymentComplete(String bookingReference, String paymentId) {
        log.info("Processing payment complete for booking: {}, payment: {}", bookingReference, paymentId);

        Booking booking = bookingRepository.findByBookingReference(bookingReference)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with reference: " + bookingReference));

        if (booking.getStatus() != BookingStatus.CONFIRMED && booking.getStatus() != BookingStatus.PENDING) {
            throw new BookingException("Invalid booking status for payment completion: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.PAID);
        bookingRepository.save(booking);

        List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingId(booking.getId());
        List<Long> seatIds = bookingSeats.stream()
                .map(BookingSeat::getSeatId)
                .collect(Collectors.toList());

        // Publish booking completed event
        BookingCompletedEvent event = BookingCompletedEvent.builder()
                .bookingReference(booking.getBookingReference())
                .userId(booking.getUserId())
                .showId(booking.getShowId())
                .theaterId(booking.getTheaterId())
                .seatIds(seatIds)
                .paymentId(paymentId)
                .completedAt(LocalDateTime.now())
                .build();

        kafkaTemplate.send(bookingCompletedTopic, booking.getBookingReference(), event);
        log.info("Published booking completed event: {}", booking.getBookingReference());
    }

    @Override
    @Transactional
    @Scheduled(fixedDelayString = "${booking.expiry.check.interval:60000}")
    public void processExpiredBookings() {
        log.info("Processing expired bookings");

        List<Booking> expiredBookings = bookingRepository.findExpiredBookings(
                BookingStatus.PENDING, LocalDateTime.now());

        for (Booking booking : expiredBookings) {
            booking.setStatus(BookingStatus.EXPIRED);
            bookingRepository.save(booking);

            List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingId(booking.getId());
            List<Long> seatIds = bookingSeats.stream()
                    .map(BookingSeat::getSeatId)
                    .collect(Collectors.toList());

            // Publish booking canceled event
            BookingCancelledEvent event = BookingCancelledEvent.builder()
                    .bookingReference(booking.getBookingReference())
                    .showId(booking.getShowId())
                    .theaterId(booking.getTheaterId())
                    .seatIds(seatIds)
                    .reason("Booking expired")
                    .canceledAt(LocalDateTime.now())
                    .build();

            kafkaTemplate.send(bookingCanceledTopic, booking.getBookingReference(), event);
            log.info("Published booking expired event: {}", booking.getBookingReference());
        }

        log.info("Processed {} expired bookings", expiredBookings.size());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getBookedSeatsByShow(Long showId) {
        log.info("Fetching booked seats for show: {}", showId);
        return bookingSeatRepository.findBookedSeatIdsByShowId(showId);
    }

    private BookingDTO mapToBookingDTO(Booking booking, List<SeatBookingDTO> seats) {
        return BookingDTO.builder()
                .id(booking.getId())
                .bookingReference(booking.getBookingReference())
                .userId(booking.getUserId())
                .showId(booking.getShowId())
                .theaterId(booking.getTheaterId())
                .showTime(booking.getShowTime())
                .ticketCount(booking.getTicketCount())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus())
                .expiryTime(booking.getExpiryTime())
                .seats(seats)
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }

    private SeatBookingDTO mapToSeatBookingDTO(BookingSeat bookingSeat) {
        return SeatBookingDTO.builder()
                .seatId(bookingSeat.getSeatId())
                .seatNumber(bookingSeat.getSeatNumber())
                .rowName(bookingSeat.getRowName())
                .price(bookingSeat.getPrice())
                .build();
    }
}
