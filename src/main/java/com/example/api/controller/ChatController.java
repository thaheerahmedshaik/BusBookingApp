package com.example.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.api.service.ChatService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:4200") // allow Angular
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping
    public Map<String, String> chat(@RequestBody Map<String, String> payload) {
        String userMessage = payload.get("message");
        String reply = chatService.askQuestion(userMessage);

        Map<String, String> response = new HashMap<>();
        response.put("reply", reply);
        return response;
    }
}