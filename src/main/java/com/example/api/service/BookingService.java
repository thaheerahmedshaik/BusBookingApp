////package com.example.api.service;
////
////import java.io.ByteArrayOutputStream;
////import java.util.List;
////
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.mail.javamail.JavaMailSender;
////import org.springframework.mail.javamail.MimeMessageHelper;
////import org.springframework.stereotype.Service;
////
////import com.example.api.model.Booking;
////import com.example.api.model.BookingDTO;
////import com.example.api.model.BookingRequest;
////import com.example.api.model.BookingRequestDTO;
////import com.example.api.repository.BookingRepository;
////import com.example.api.repository.BookingRequestRepository;
////import com.itextpdf.text.Document;
////import com.itextpdf.text.Paragraph;
////import com.itextpdf.text.pdf.PdfWriter;
////
////import jakarta.mail.internet.MimeMessage;
////
////@Service
////public class BookingService {
////    @Autowired
////    private JavaMailSender mailSender;
////    private final BookingRepository bookingRepository;
////    private final BookingRequestRepository bookingRequestRepository;
////
////    public BookingService(BookingRepository bookingRepository,
////                          BookingRequestRepository bookingRequestRepository) {
////        this.bookingRepository = bookingRepository;
////        this.bookingRequestRepository = bookingRequestRepository;
////    }
////
////    public Booking saveBooking(Booking booking, List<BookingRequest> passengers) {
////        // Link passengers to booking
////        passengers.forEach(p -> p.setBooking(booking));
////        booking.setPassengers(passengers);
////
////        // Save booking (cascade will save passengers)
////        return bookingRepository.save(booking);
////    }
//// // Generate PDF Ticket
////    public byte[] generateBookingPdf(BookingDTO booking) throws Exception {
////        ByteArrayOutputStream out = new ByteArrayOutputStream();
////        Document doc = new Document();
////        PdfWriter.getInstance(doc, out);
////        doc.open();
////
////        doc.add(new Paragraph("Bus Booking Confirmation", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD)));
////        doc.add(new Paragraph("Booking ID: " + booking.getId()));
////        doc.add(new Paragraph("Bus: " + booking.getBusName()));
////        doc.add(new Paragraph("From: " + booking.getBoardingPoint() + "  →  To: " + booking.getDroppingPoint()));
////        doc.add(new Paragraph("Date: " + booking.getTravelDate()));
////        doc.add(new Paragraph("Total Fare: ₹" + booking.getTotalAmount()));
////        doc.add(new Paragraph("\nPassengers:"));
////
////        for (PassengerDTO p : booking.getPassengers()) {
////            doc.add(new Paragraph(
////                "• " + p.getName() + " (" + p.getGender() + ", " + p.getAge() + " yrs) - Seat " + p.getSeatNumber()
////            ));
////        }
////
////        doc.close();
////        return out.toByteArray();
////    }
////
////    // Send Email with PDF Attachment
////    public void sendBookingEmail(BookingDTO booking, byte[] pdfBytes) throws Exception {
////        MimeMessage message = mailSender.createMimeMessage();
////        MimeMessageHelper helper = new MimeMessageHelper(message, true);
////
////        helper.setTo(booking.getContact().getEmail());
////        helper.setSubject("Your Bus Booking Confirmation - " + booking.getBusName());
////        helper.setText(
////            "Dear " + booking.getPassengers().get(0).getName() + ",\n\n" +
////            "Your booking from " + booking.getBoardingPoint() + " to " + booking.getDroppingPoint() +
////            " is confirmed.\nPlease find your ticket attached.\n\nThank you for choosing us!"
////        );
////
////        helper.addAttachment("Ticket_" + booking.getId() + ".pdf", new ByteArrayResource(pdfBytes));
////
////        mailSender.send(message);
////    }
////
////    // Optional: Send SMS or WhatsApp (Twilio or Fast2SMS API)
////    public void sendBookingSms(Booking booking) {
////        String msg = "Your bus booking from " + booking.getBoardingPoint() + " to " +
////                     booking.getDroppingPoint() + " is confirmed. Total ₹" + booking.getTotalAmount();
////        //System.out.println("📱 Sending SMS to " + booking.getPhone() + ": " + msg);
////        // Integrate Twilio/Fast2SMS API here
////    }
////}
//
package com.example.api.service;

import com.example.api.model.*;
import com.example.api.repository.BookingRepository;
import com.example.api.repository.BookingRequestRepository;
import com.example.api.repository.BusRepository;
import com.example.api.repository.SeatRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingRequestRepository bookingRequestRepository;
    private final BusRepository busRepository;
    private final SeatRepository seatRepository;
    private final JavaMailSender mailSender;

    @Autowired
    public BookingService(BookingRepository bookingRepository,
                          BookingRequestRepository bookingRequestRepository,
                          BusRepository busRepository,
                          SeatRepository seatRepository,
                          JavaMailSender mailSender) {
        this.bookingRepository = bookingRepository;
        this.bookingRequestRepository = bookingRequestRepository;
        this.busRepository = busRepository;
        this.seatRepository = seatRepository;
        this.mailSender = mailSender;
    }

    /**
     * ✅ Save Booking and Passenger Data
     */
    @Transactional
    public Booking saveBooking(BookingDTO dto) {
        Bus bus = busRepository.findById(dto.getBusId())
                .orElseThrow(() -> new RuntimeException("Bus not found"));

        if (bus.getArrivalTime() == null || bus.getDepartureTime() == null) {
            throw new IllegalStateException("Bus schedule incomplete");
        }

        Booking booking = new Booking();
        booking.setBusId(bus.getId());
        booking.setBusName(bus.getBusName());
        booking.setFromCity(bus.getFromCity());
        booking.setToCity(bus.getToCity());
        booking.setDepartureTime(bus.getDepartureTime());
        booking.setArrivalTime(bus.getArrivalTime());
        booking.setDuration(bus.getDuration());
        booking.setBoardingPoint(dto.getBoardingPoint());
        booking.setDroppingPoint(dto.getDroppingPoint());
        booking.setTravelDate(bus.getDate() != null ? bus.getDate() : LocalDate.now());

        // ✅ Lookup seats by seat codes
        List<Seat> seats = seatRepository.findByNumberIn(dto.getSeatCodes());
        double totalAmount = seats.stream().mapToDouble(Seat::getPrice).sum();
        booking.setTotalAmount(totalAmount);
        booking.setPrice(totalAmount);

        Booking savedBooking = bookingRepository.save(booking);

        // ✅ Map passengers to seat codes
        List<BookingRequest> passengers = dto.getPassengers().stream()
                .map(p -> {
                    BookingRequest br = new BookingRequest();
                    br.setName(p.getName());
                    br.setAge(p.getAge());
                    br.setPhone(p.getPhone());
                    br.setState(p.getState());
                    br.setSeatCode(p.getSeatCode()); // use seatCode instead of seatId
                    br.setBooking(savedBooking);
                    return br;
                }).collect(Collectors.toList());

        bookingRequestRepository.saveAll(passengers);
        savedBooking.setPassengers(passengers);

        // Mark seats unavailable
        seats.forEach(seat -> {
            seat.setAvailable(false);
            seatRepository.save(seat);
        });

        return savedBooking;
    }



    /**
     * ✅ Generate Ticket PDF
     */
    public byte[] generateBookingPdf(BookingDTO booking) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4);
        PdfWriter.getInstance(doc, out);
        doc.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);

        doc.add(new Paragraph("🚌 Bus Booking Confirmation", titleFont));
        doc.add(new Paragraph("Bus: " + booking.getBusId()));
        doc.add(new Paragraph("From: " + booking.getBoardingPoint() + " → To: " + booking.getDroppingPoint(), normalFont));
        doc.add(new Paragraph("Date: " + LocalDate.now(), normalFont));
        doc.add(new Paragraph("\nPassengers:", normalFont));

        for (BookingRequestDTO p : booking.getPassengers()) {
            doc.add(new Paragraph("• " + p.getName() + " (" + p.getAge() + " yrs) - Seat " + p.getSeatCode(), normalFont));
            doc.add(new Paragraph("  Phone: " + p.getPhone() + ", State: " + p.getState(), normalFont));
        }

        doc.add(new Paragraph("\nThank you for booking with us!", normalFont));

        doc.close();
        return out.toByteArray();
    }

    /**
     * ✅ Send Email with PDF Attachment
     */
    public void sendBookingEmail(String toEmail, BookingDTO booking, byte[] pdfBytes) throws Exception {
        if (toEmail == null || toEmail.isEmpty()) {
            System.out.println("⚠️ Email not provided — skipping email sending.");
            return;
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("Your Bus Booking Confirmation - " + booking.getBoardingPoint() + " → " + booking.getDroppingPoint());
        helper.setText("Dear " + (booking.getPassengers().isEmpty() ? "Customer" : booking.getPassengers().get(0).getName()) +
                ",\n\nYour booking is confirmed. Please find your ticket attached.\n\nHappy Journey!");

        helper.addAttachment("Booking_Ticket.pdf", new ByteArrayResource(pdfBytes));
        mailSender.send(message);

        System.out.println("✅ Email sent successfully to " + toEmail);
    }

    /**
     * ✅ Send SMS notification (stub)
     */
    public void sendBookingSms(String phone, BookingDTO booking) {
        if (phone == null || phone.isEmpty()) {
            System.out.println("⚠️ Phone not provided — skipping SMS.");
            return;
        }

        String msg = "Your bus booking from " + booking.getBoardingPoint() +
                " to " + booking.getDroppingPoint() +
                " is confirmed. Date: " + LocalDate.now() + ".";

        System.out.println("📱 Sending SMS to " + phone + ": " + msg);
        // Integrate Twilio / Fast2SMS here
    }
}
