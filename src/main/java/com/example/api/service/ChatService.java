package com.example.api.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.api.model.ChatRequest;
import com.example.api.model.ChatResponse;

import java.util.Collections;

@Service
public class ChatService {

    private static final String CB_NAME = "chatServiceCB";

    @Value("${azure.openai.endpoint}")
    private String endpoint;

    @Value("${azure.openai.api-key}")
    private String apiKey;

    @Value("${azure.openai.deployment-id}")
    private String deploymentId;

    @Value("${azure.openai.api-version}")
    private String apiVersion;

    private final RestTemplate restTemplate = new RestTemplate();

    @CircuitBreaker(name = CB_NAME, fallbackMethod = "fallbackAskQuestion")
    public String askQuestion(String question) {
        try {
            String url = endpoint + "/openai/deployments/" + deploymentId + "/chat/completions?api-version=" + apiVersion;

            ChatRequest.Message userMessage = new ChatRequest.Message("user", question);
            ChatRequest request = new ChatRequest(deploymentId, Collections.singletonList(userMessage));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", apiKey);

            HttpEntity<ChatRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<ChatResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, ChatResponse.class);

            if (response.getBody() != null && response.getBody().getChoices() != null && !response.getBody().getChoices().isEmpty()) {
                return response.getBody().getChoices().get(0).getMessage().getContent();
            }
        } catch (Exception e) {
            throw new RuntimeException("Azure OpenAI API call failed", e);
        }

        return "Sorry, I couldn't find an answer.";
    }

    // Fallback
    public String fallbackAskQuestion(String question, Throwable t) {
        System.out.println("ChatService fallbackAskQuestion triggered: " + t.getMessage());
        return "Chat service temporarily unavailable. Please try again later.";
    }
}
