package com.example.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailTestRunner implements CommandLineRunner {

    @Autowired
    private JavaMailSender mailSender;


    /**
     * アプリケーション起動時に自動的に実行される処理。
     * Gmail SMTP の設定が正しく動作しているかを確認するため、
     * テストメールを送信する。
     */
    @Override
    public void run(String... args) {
        try {
            // シンプルなテキストメールを作成
            SimpleMailMessage message = new SimpleMailMessage();

            // 受信者のメールアドレス（自分のメールに変更すること）
            message.setTo("receiver_email@example.com");

            // メール件名
            message.setSubject("Test Email from Spring Boot");

            // メール本文
            message.setText("Hello! This is a test email confirming that your Gmail SMTP works.");

            // 送信者（spring.mail.username と一致している必要がある）
            message.setFrom("yourmail@gmail.com");

            // メール送信
            mailSender.send(message);

            System.out.println("テストメールを正常に送信しました！");
        } catch (Exception e) {
            // 送信失敗時のエラーログ
            System.err.println(" テストメールの送信に失敗しました: " + e.getMessage());
            e.printStackTrace();
        }
    }
}