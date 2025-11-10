package com.example.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.model.BookingRequest;
import com.example.api.service.WhatsAppService;

@RestController
@RequestMapping("/api/whatsapp")
@CrossOrigin(origins = "http://localhost:4200")
public class WhatsAppController {

    private final WhatsAppService whatsAppService;

    public WhatsAppController(WhatsAppService whatsAppService) {
        this.whatsAppService = whatsAppService;
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendMessage(@RequestBody BookingRequest booking) {
        Map<String, String> response = new HashMap<>();
        try {
            String sid = whatsAppService.sendBookingDetails(booking); // Return SID
            response.put("status", "success");
            response.put("sid", sid);
            return ResponseEntity.ok(response); // JSON response
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
