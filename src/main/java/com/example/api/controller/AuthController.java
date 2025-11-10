package com.example.api.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.model.LoginRequest;
import com.example.api.model.User;
import com.example.api.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")

@CrossOrigin(origins = "http://localhost:4200") // Angular からのアクセスを許可
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     *  ログイン処理
     * ユーザー名とパスワードを認証し、成功/失敗を返す。
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // ユーザー名とパスワードを使って Spring Security による認証を実行
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // 認証成功の場合、メッセージを返す
            return ResponseEntity.ok(new HashMap<String, String>() {{
                put("message", "Login Successful");
            }});
        } catch (BadCredentialsException e) {
            // 認証失敗時（パスワードやユーザー名が間違っている場合）
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
        }
    }

    /**
     * ユーザー登録処理
     * 新しいユーザーをデータベースに保存する。
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequest request) {

        // 既に同じユーザー名が存在する場合はエラーを返す
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        
        // 新規ユーザーの作成
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // パスワードをハッシュ化
        user.setRole("USER"); // デフォルトロールを設定

        // DB に保存
        userRepository.save(user);

        // 成功メッセージを返す
        return ResponseEntity.ok("User registered successfully");
    }
}

