package com.example.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.api.model.*;
import com.example.api.repository.BusRepository;
import com.example.api.service.BusService;
import com.example.api.service.SeatService;
import com.example.api.service.BusPointService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/buses")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Bus Controller", description = "Operations related to buses")
public class BusController {

    private final BusRepository busRepository;
    private final BusService busService;
    private final SeatService seatService;
    private final BusPointService busPointService;


    // コンストラクタ依存性注入
    public BusController(BusRepository busRepository,
                         BusService busService,
                         SeatService seatService,
                         BusPointService busPointService) {
        this.busRepository = busRepository;
        this.busService = busService;
        this.seatService = seatService;
        this.busPointService = busPointService;
    }

    // 出発都市一覧取得
    @Operation(summary = "Get all departure cities", description = "Returns a list of distinct departure cities")
    @GetMapping("/fromCities")
    public ResponseEntity<List<String>> getFromCities() {
        List<String> fromCities = busRepository.findDistinctFromCities();
        return ResponseEntity.ok(fromCities);
    }

    // 到着都市一覧取得
    @Operation(summary = "Get all destination cities", description = "Returns a list of distinct destination cities")
    @GetMapping("/toCities")
    public ResponseEntity<List<String>> getToCities() {
        List<String> toCities = busRepository.findDistinctToCities();
        return ResponseEntity.ok(toCities);
    }

    // バス検索（都市＋日付オプション）
    @Operation(summary = "Search buses", description = "Search buses by from city, to city and optional date (yyyy-MM-dd)")
    @GetMapping("/search")
    public ResponseEntity<List<Bus>> searchBuses(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam(required = false) String date) {

        try {
            List<Bus> buses;
            if (date != null && !date.isEmpty()) {
                LocalDate parsedDate = LocalDate.parse(date);
                buses = busRepository.findByFromCityAndToCityAndDate(from, to, parsedDate);
            } else {
                buses = busRepository.findByFromCityAndToCity(from, to);
            }
            return ResponseEntity.ok(buses);

        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 全バス取得
    @Operation(summary = "Get all buses", description = "Returns a list of all buses")
    @GetMapping
    public ResponseEntity<List<Bus>> getAllBuses() {
        return ResponseEntity.ok(busService.getAllBuses());
    }

    // ID によるバス取得
    @Operation(summary = "Get bus by ID", description = "Returns details of a bus by its ID")
    @GetMapping("/id/{busId}")
    public ResponseEntity<Bus> getBusById(@PathVariable Long busId) {
        Bus bus = busService.getBusById(busId);
        if (bus != null) {
            return ResponseEntity.ok(bus);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // バスの座席一覧取得
    @Operation(summary = "Get seats for a bus", description = "Returns list of seats for a given bus ID")
    @GetMapping("/{busId}/seats")
    public ResponseEntity<List<Seat>> getSeatsForBus(@PathVariable Long busId) {
        List<Seat> seats = seatService.getSeatsByBusId(busId);
        return ResponseEntity.ok(seats);
    }

    // 座席予約処理
    @Operation(summary = "Book seats on a bus", description = "Book seats by providing busId, seatIds, and passenger details")
    @PostMapping("/bookSeat")
    public ResponseEntity<?> bookSeat(@RequestBody BookingDTO dto) {
        try {
            // バス取得
            Bus bus = busService.getBusById(dto.getBusId());
            if (bus == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bus not found");

            // 座席チェック
            if (dto.getSeatIds() == null || dto.getSeatIds().isEmpty())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No seats selected");

            List<Seat> seats = seatService.getSeatsByIds(dto.getSeatIds());
            if (seats.size() != dto.getSeatIds().size() || seats.stream().anyMatch(s -> !s.isAvailable()))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("One or more seats are not available");

            // 乗客数チェック
            if (dto.getPassengers() == null || dto.getPassengers().size() != seats.size())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passenger count must match selected seats");

            // 予約作成
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

            double totalAmount = seats.stream().mapToDouble(Seat::getPrice).sum();
            booking.setTotalAmount(totalAmount);
            booking.setPrice(totalAmount);
            booking.setTravelDate(LocalDate.now());

            // ✅ 乗客追加
            List<BookingRequest> passengers = new ArrayList<>();
            for (int i = 0; i < dto.getPassengers().size(); i++) {
                BookingRequestDTO p = dto.getPassengers().get(i);
                BookingRequest br = new BookingRequest();
                br.setName(p.getName());
                br.setAge(p.getAge());
                br.setPhone(p.getPhone());
                br.setState(p.getState());
                br.setSeatId(seats.get(i).getId());
                br.setBooking(booking);
                passengers.add(br);
            }
            booking.setPassengers(passengers);

            // ✅ 予約保存＋座席更新
            Booking savedBooking = busService.saveBooking(booking, seats);
            seats.forEach(seat -> {
                seat.setAvailable(false);
                seatService.saveSeat(seat);
            });

            return ResponseEntity.ok(savedBooking);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Booking failed: " + e.getMessage());
        }
    }

    // 乗車ポイント一覧取得
    @GetMapping("/{busId}/boarding")
    public List<BusPoint> getBoardingPoints(@PathVariable Long busId) {
        return busPointService.getBoardingPoints(busId);
    }

    // 降車ポイント一覧取得
    @GetMapping("/{busId}/dropping")
    public List<BusPoint> getDroppingPoints(@PathVariable Long busId) {
        return busPointService.getDroppingPoints(busId);
    }

    // 新しいポイント追加
    @PostMapping("/points")
    public BusPoint addPoint(@RequestBody BusPoint point) {
        return busPointService.addPoint(point);
    }

    // 全ポイント取得
    @GetMapping("/{busId}/points/all")
    public List<BusPoint> getAllPoints(@PathVariable Long busId) {
        return busPointService.getAllPoints(busId);
    }
}
