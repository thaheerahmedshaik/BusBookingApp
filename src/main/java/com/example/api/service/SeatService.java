package com.example.api.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import com.example.api.model.Seat;
import com.example.api.repository.SeatRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private static final String CB_NAME = "seatServiceCB";

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    // 1️⃣ Fetch ALL seats for a bus
    @CircuitBreaker(name = CB_NAME, fallbackMethod = "fallbackSeatsByBusId")
    public List<Seat> getSeatsByBusId(Long busId) {
        return seatRepository.findByBusId(busId);
    }

    // 2️⃣ Fetch seats for a bus filtered by seat codes
    @CircuitBreaker(name = CB_NAME, fallbackMethod = "fallbackSeatsByBusIdAndCodes")
    public List<Seat> getSeatsByBusId(Long busId, List<String> seatCodes) {
        return seatRepository.findByBusIdAndNumberIn(busId, seatCodes);
    }

    // Fallbacks
    public List<Seat> fallbackSeatsByBusId(Long busId, Throwable t) {
        System.out.println("SeatService fallbackSeatsByBusId triggered: " + t.getMessage());
        return List.of();
    }

    public List<Seat> fallbackSeatsByBusIdAndCodes(Long busId, List<String> seatCodes, Throwable t) {
        System.out.println("SeatService fallbackSeatsByBusIdAndCodes triggered: " + t.getMessage());
        return List.of();
    }

    // ✅ Save/update a seat
    @CircuitBreaker(name = CB_NAME, fallbackMethod = "fallbackSaveSeat")
    public Seat saveSeat(Seat seat) {
        return seatRepository.save(seat);
    }

    public Seat fallbackSaveSeat(Seat seat, Throwable t) {
        System.out.println("SeatService fallbackSaveSeat triggered: " + t.getMessage());
        return null;
    }

    // ✅ Get seat by ID
    public Seat getSeatById(Long seatId) {
        Optional<Seat> seatOpt = seatRepository.findById(seatId);
        return seatOpt.orElseThrow(() -> new RuntimeException("Seat not found with id: " + seatId));
    }

    // ✅ Get seats by numeric IDs
    public List<Seat> getSeatsByIds(List<Long> seatIds) {
        return seatRepository.findAllById(seatIds);
    }

    // ✅ Get seats by codes only
    public List<Seat> getSeatsByCodes(List<String> seatCodes) {
        return seatRepository.findByNumberIn(seatCodes);
    }

    // ✅ Explicit method for busId + codes
    public List<Seat> getSeatsByBusIdAndCodes(Long busId, List<String> codes) {
        return seatRepository.findByBusIdAndNumberIn(busId, codes);
    }
}
