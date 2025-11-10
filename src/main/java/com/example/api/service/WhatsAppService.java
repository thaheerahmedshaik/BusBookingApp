package com.example.api.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.api.model.BookingRequest;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.annotation.PostConstruct;

@Service
public class WhatsAppService {

    private static final String CB_NAME = "whatsappServiceCB";

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.whatsapp.from}")
    private String whatsappFrom;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    @CircuitBreaker(name = CB_NAME, fallbackMethod = "fallbackSendBookingDetails")
    public String sendBookingDetails(BookingRequest booking) {
        try {
            String toNumber = booking.getPhone();
            String messageBody = "Hello " + booking.getName() +
                                 "! Age: " + booking.getAge() +
                                 ", State: " + booking.getState() +
                                 ". Thank you for registering.";

            Message message = Message.creator(
                    new PhoneNumber("whatsapp:" + toNumber),
                    new PhoneNumber(whatsappFrom),
                    messageBody
            ).create();

            return "Message sent! SID: " + message.getSid();
        } catch (Exception e) {
            throw new RuntimeException("Failed to send WhatsApp message", e);
        }
    }

    // Fallback
    public String fallbackSendBookingDetails(BookingRequest booking, Throwable t) {
        System.out.println("WhatsAppService fallbackSendBookingDetails triggered: " + t.getMessage());
        return "Failed to send WhatsApp message for " + booking.getName();
    }
}
