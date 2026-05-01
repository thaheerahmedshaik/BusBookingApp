package com.example.api.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/buses")
@CrossOrigin(origins = {
	    "http://localhost:4200",
	    "https://lemon-forest-0051cc5007.azurestaticapps.net"
	}) // // allow Angular frontend
@Tag(name = "Bus Controller", description = "Operations related to buses")
public class BusController {

    private final BusRepository busRepository;
    private final BusService busService;
    private final SeatService seatService;
    private final BusPointService busPointService;

    public BusController(BusRepository busRepository,
                         BusService busService,
                         SeatService seatService,
                         BusPointService busPointService) {
        this.busRepository = busRepository;
        this.busService = busService;
        this.seatService = seatService;
        this.busPointService = busPointService;
    }

    @Operation(summary = "Get all departure cities")
    @GetMapping("/fromCities")
    public ResponseEntity<List<String>> getFromCities() {
        return ResponseEntity.ok(busRepository.findDistinctFromCities());
    }

    @Operation(summary = "Get all destination cities")
    @GetMapping("/toCities")
    public ResponseEntity<List<String>> getToCities() {
        return ResponseEntity.ok(busRepository.findDistinctToCities());
    }

    @Operation(summary = "Search buses")
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Bus>> searchBuses(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam(required = false) String date) {
        try {
            List<Bus> buses;
            if (date != null && !date.isEmpty()) {
                LocalDate parsedDate = parseDateFlexible(date);
                buses = busRepository.findByFromCityAndToCityAndDate(from.trim(), to.trim(), parsedDate);
            } else {
                buses = busRepository.findByFromCityAndToCity(from.trim(), to.trim());
            }
            return ResponseEntity.ok(buses);
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    private LocalDate parseDateFlexible(String dateStr) {
        List<DateTimeFormatter> formatters = List.of(
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd")
        );
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException ignored) {}
        }
        throw new DateTimeParseException("Invalid date format", dateStr, 0);
    }

    @Operation(summary = "Get all buses")
    @GetMapping
    public ResponseEntity<List<Bus>> getAllBuses() {
        return ResponseEntity.ok(busService.getAllBuses());
    }

    @Operation(summary = "Get bus by ID")
    @GetMapping("/id/{busId}")
    public ResponseEntity<Bus> getBusById(@PathVariable Long busId) {
        Bus bus = busService.getBusById(busId);
        return (bus != null) ? ResponseEntity.ok(bus)
                             : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    @Operation(summary = "Get seats for a bus")
    @GetMapping("/{busId}/seats")
    public ResponseEntity<List<Seat>> getSeatsForBus(@PathVariable Long busId) {
        return ResponseEntity.ok(seatService.getSeatsByBusId(busId));
    }
    
 // Optional: filter by codes
    @GetMapping("/{busId}/seats/byCodes")
    public ResponseEntity<List<Seat>> getSeatsByCodes(@PathVariable Long busId,
                                                      @RequestParam List<String> codes) {
        return ResponseEntity.ok(seatService.getSeatsByBusId(busId, codes));
    }

    

    @PostMapping("/bookSeat")
    public ResponseEntity<?> bookSeat(@Valid @RequestBody BookingDTO dto) {
        try {
            Bus bus = busService.getBusById(dto.getBusId());
            if (bus == null) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Bus not found", 400));
            }

            // ✅ Lookup seats by codes
            List<Seat> seats = seatService.getSeatsByCodes(dto.getSeatCodes());
            if (seats.isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Invalid seat codes", 400));
            }

            Booking booking = new Booking();
            booking.setBusId(bus.getId());
            booking.setBusName(bus.getBusName());
            booking.setFromCity(bus.getFromCity());
            booking.setToCity(bus.getToCity());
            booking.setBoardingPoint(dto.getBoardingPoint());
            booking.setDroppingPoint(dto.getDroppingPoint());
            booking.setDepartureTime(bus.getDepartureTime());
            booking.setArrivalTime(bus.getArrivalTime());
            booking.setDuration(bus.getDuration());
           
            booking.setTravelDate(bus.getDate() != null ? bus.getDate() : LocalDate.now());

            double totalAmount = seats.stream().mapToDouble(Seat::getPrice).sum();
            booking.setTotalAmount(totalAmount);
            booking.setPrice(totalAmount);

            List<BookingRequest> passengers = new ArrayList<>();
            for (BookingRequestDTO p : dto.getPassengers()) {
                BookingRequest br = new BookingRequest();
                br.setName(p.getName());
                br.setAge(p.getAge());
                br.setPhone(p.getPhone());
                br.setState(p.getState());
                br.setSeatCode(p.getSeatCode()); // ✅ use seatCode
                br.setBooking(booking);
                passengers.add(br);
            }
            booking.setPassengers(passengers);

            Booking savedBooking = busService.saveBooking(booking, seats);
            return ResponseEntity.ok(savedBooking);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Booking failed: " + e.getMessage(), 500));
        }
    }

    @GetMapping("/{busId}/boarding")
    public List<BusPoint> getBoardingPoints(@PathVariable Long busId) {
        return busPointService.getBoardingPoints(busId);
    }

    @GetMapping("/{busId}/dropping")
    public List<BusPoint> getDroppingPoints(@PathVariable Long busId) {
        return busPointService.getDroppingPoints(busId);
    }

    @PostMapping("/points")
    public BusPoint addPoint(@RequestBody BusPoint point) {
        return busPointService.addPoint(point);
    }

    @GetMapping("/{busId}/points/all")
    public List<BusPoint> getAllPoints(@PathVariable Long busId) {
        return busPointService.getAllPoints(busId);
    }
  
    
    @GetMapping("/filter")
    public ResponseEntity<List<Bus>> filterBuses(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String seatType,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) String amenities
    ) {

        try {
            // ✅ safer date parsing
            LocalDate parsedDate = (date != null && !date.trim().isEmpty())
                    ? parseDateFlexible(date)
                    : null;

            // ✅ call service
            List<Bus> buses = busService.filterBuses(
                    from.trim(),
                    to.trim(),
                    parsedDate,
                    seatType,
                    minPrice,
                    maxPrice,
                    minRating,
                    amenities
            );

            return ResponseEntity.ok(buses);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
