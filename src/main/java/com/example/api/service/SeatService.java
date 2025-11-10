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

    @CircuitBreaker(name = CB_NAME, fallbackMethod = "fallbackSeatsByBusId")
    public List<Seat> getSeatsByBusId(Long busId) {
        return seatRepository.findByBusId(busId);
    }

    // Fallback
    public List<Seat> fallbackSeatsByBusId(Long busId, Throwable t) {
        System.out.println("SeatService fallbackSeatsByBusId triggered: " + t.getMessage());
        return List.of();
    }

    // ✅ Add this method to save/update a seat
    @CircuitBreaker(name = CB_NAME, fallbackMethod = "fallbackSaveSeat")
    public Seat saveSeat(Seat seat) {
        return seatRepository.save(seat);
    }

    // Fallback for saveSeat
    public Seat fallbackSaveSeat(Seat seat, Throwable t) {
        System.out.println("SeatService fallbackSaveSeat triggered: " + t.getMessage());
        return null;
    }
    public Seat getSeatById(Long seatId) {
        Optional<Seat> seatOpt = seatRepository.findById(seatId);
        if (seatOpt.isPresent()) {
            return seatOpt.get();
        } else {
            throw new RuntimeException("Seat not found with id: " + seatId);
        }
    }

    public List<Seat> getSeatsByIds(List<Long> seatIds) {
        return seatRepository.findAllById(seatIds);
    }
}
