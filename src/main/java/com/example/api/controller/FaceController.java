package com.example.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.api.service.AzureFaceService;

@RestController
@RequestMapping("/api/face")
public class FaceController {

    @Autowired
    private AzureFaceService azureFaceService;

    // ✅ Register face → returns faceId
    @PostMapping("/register")
    public String registerFace(@RequestParam String imageUrl) {
        return azureFaceService.detectFace(imageUrl);
    }

    // ✅ Verify 2 faces → returns true/false
    @PostMapping("/verify")
    public boolean verifyFaces(@RequestParam String faceId1,
                               @RequestParam String faceId2) {
        return azureFaceService.verifyFaces(faceId1, faceId2);
    }
}
