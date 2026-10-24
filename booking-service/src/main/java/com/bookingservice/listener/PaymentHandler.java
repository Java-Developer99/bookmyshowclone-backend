package com.bookingservice.listener;

import com.bookingservice.service.BookingService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentHandler {

    private final BookingService bookingService;

    @KafkaListener(topics = "${kafka.topics.payment-completed}", groupId = "${spring.kafka.consumer.group-id}")
    public void handlePaymentCompleted(JsonNode event) {
        try {
            String bookingReference = event.get("bookingReference").asText();
            String paymentId = event.get("paymentId").asText();

            log.info("Received payment completed event: bookingReference={}, paymentId={}",
                    bookingReference, paymentId);

            bookingService.processPaymentComplete(bookingReference, paymentId);
        } catch (Exception e) {
            log.error("Error processing payment completed event", e);
        }
    }
}
