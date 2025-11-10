package com.example.api.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.example.api.model.Booking;
import com.example.api.model.Bus;
import com.example.api.model.Seat;
import com.example.api.repository.BookingRepository;
import com.example.api.repository.BusRepository;
import com.example.api.repository.SeatRepository;

import java.util.List;

@Service
public class BusService {

    private final BusRepository busRepository;
    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;

    private static final String CB_NAME = "busServiceCB";

    public BusService(BusRepository busRepository,
                      BookingRepository bookingRepository,
                      SeatRepository seatRepository) {
        this.busRepository = busRepository;
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
    }

    // ---------------- Bus Methods ----------------
    @CircuitBreaker(name = CB_NAME, fallbackMethod = "fallbackGetAllBuses")
    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }

    @CircuitBreaker(name = CB_NAME, fallbackMethod = "fallbackGetBusById")
    public Bus getBusById(Long id) {
        return busRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bus not found with id: " + id));
    }

    @CircuitBreaker(name = CB_NAME, fallbackMethod = "fallbackSaveBus")
    public Bus saveBus(Bus bus) {
        return busRepository.save(bus);
    }

    @CircuitBreaker(name = CB_NAME, fallbackMethod = "fallbackDeleteBus")
    public void deleteBus(Long id) {
        busRepository.deleteById(id);
    }

    // ---------------- Booking Methods ----------------
    @Transactional
    public Booking saveBooking(Booking booking, List<Seat> bookedSeats) {
        // Save booking + passengers
        Booking savedBooking = bookingRepository.save(booking);

        // Mark seats unavailable
        for (Seat seat : bookedSeats) {
            seat.setAvailable(false);
            seatRepository.save(seat);
        }

        return savedBooking;
    }

    // ---------------- Fallback Methods ----------------
    public List<Bus> fallbackGetAllBuses(Throwable t) {
        System.out.println("BusService fallbackGetAllBuses triggered: " + t.getMessage());
        return List.of();
    }

    public Bus fallbackGetBusById(Long id, Throwable t) {
        System.out.println("BusService fallbackGetBusById triggered: " + t.getMessage());
        Bus bus = new Bus();
        bus.setId(id);
        return bus;
    }

    public Bus fallbackSaveBus(Bus bus, Throwable t) {
        System.out.println("BusService fallbackSaveBus triggered: " + t.getMessage());
        return bus;
    }

    public void fallbackDeleteBus(Long id, Throwable t) {
        System.out.println("BusService fallbackDeleteBus triggered: " + t.getMessage());
    }
}
