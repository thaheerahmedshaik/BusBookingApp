package com.example.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.api.model.Booking;
import com.example.api.model.BookingDTO;
import com.example.api.model.BookingRequestDTO;
import com.example.api.service.BookingService;

@RestController
@RequestMapping("/buses")

@CrossOrigin(origins = "http://localhost:4200") // Angular からのアクセスを許可
public class BookingController {

    @Autowired
    private BookingService bookingService;

    /**
     *  予約を確定し、以下の処理を一括で実行する:
     * 1. 予約データと乗客情報を DB に保存
     * 2. チケット PDF を生成
     * 3. メール送信（PDF 添付）
     * 4. SMS 通知
     * 5. PDF をフロントエンドへ返却（ダウンロード用）
     */
    @PostMapping("/confirm")
    public ResponseEntity<byte[]> confirmBooking(@RequestBody BookingDTO bookingDTO) throws Exception {

        // 1️⃣ 予約内容（バス情報 + 乗客情報）をデータベースへ保存
        Booking savedBooking = bookingService.saveBooking(bookingDTO);

        // 2️⃣ チケット PDF を生成（バイトデータとして返される）
        byte[] pdfBytes = bookingService.generateBookingPdf(bookingDTO);

        // 3️ 予約確認メールを送信
        //    ※ 1人目の乗客のメールアドレスを仮に使用（本番は適宜変更）
        String email = null;
        if (bookingDTO.getPassengers() != null && !bookingDTO.getPassengers().isEmpty()) {
            // TODO: 本来は乗客のメールを使用する。以下は仮のロジック（修正推奨）
            email = bookingDTO.getPassengers().get(0).getPhone() + "skthaheerahmed1996@gmail.com";
        }
        bookingService.sendBookingEmail(email, bookingDTO, pdfBytes);

        // 4️ 同じく 1人目の乗客へ SMS 通知を送信
        String phone = null;
        if (bookingDTO.getPassengers() != null && !bookingDTO.getPassengers().isEmpty()) {
            phone = bookingDTO.getPassengers().get(0).getPhone();
        }
        bookingService.sendBookingSms(phone, bookingDTO);

        // 5️ 作成した PDF をブラウザへ返し、ユーザーがダウンロードできるようにする
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=ticket_" + savedBooking.getId() + ".pdf") // ダウンロード名
                .contentType(MediaType.APPLICATION_PDF) // PDF MIME Type
                .body(pdfBytes); // PDF バイトデータ
}
}