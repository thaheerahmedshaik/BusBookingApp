package com.example.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.api.service.AzureFaceService;

@RestController
@RequestMapping("/api/face")
public class FaceController {

    @Autowired
    private AzureFaceService azureFaceService;

    // ✅ 顔を登録するエンドポイント（Face API を使用）
    // 画像URLから顔を検出し、faceId を返却する
    @PostMapping("/register")
    public String registerFace(@RequestParam String imageUrl) {
        return azureFaceService.detectFace(imageUrl);
    }

    // ✅ 2つの顔を比較するエンドポイント
    // faceId1 と faceId2 を照合し、同一人物なら true を返す
    @PostMapping("/verify")
    public boolean verifyFaces(@RequestParam String faceId1,
                               @RequestParam String faceId2) {
        return azureFaceService.verifyFaces(faceId1, faceId2);
    }
}
