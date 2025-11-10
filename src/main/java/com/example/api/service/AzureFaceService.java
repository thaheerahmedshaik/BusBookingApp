package com.example.api.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AzureFaceService {

    @Value("${azure.face.endpoint}")
    private String endpoint;

    @Value("${azure.face.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    // ✅ Detect face → returns faceId
    public String detectFace(String imageUrl) {
        String url = endpoint + "/face/v1.0/detect?returnFaceId=true";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Ocp-Apim-Subscription-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = Map.of("url", imageUrl);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<List> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            entity,
            List.class
        );

        List<Map<String, Object>> faces = response.getBody();

        if (faces == null || faces.isEmpty()) {
            throw new RuntimeException("No face detected!");
        }

        return faces.get(0).get("faceId").toString(); // ✅ return faceId
    }

    // ✅ Verify → returns true/false
    public boolean verifyFaces(String faceId1, String faceId2) {
        String url = endpoint + "/face/v1.0/verify";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Ocp-Apim-Subscription-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = Map.of(
                "faceId1", faceId1,
                "faceId2", faceId2
        );

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        return (Boolean) response.getBody().get("isIdentical");
    }
}
