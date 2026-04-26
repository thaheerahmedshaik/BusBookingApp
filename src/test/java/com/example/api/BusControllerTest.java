//package com.example.api;
//
//import com.example.api.controller.BusController;
//import com.example.api.model.*;
//import com.example.api.service.BusService;
//import com.example.api.service.SeatService;
//import com.example.api.service.BusPointService;
//import com.example.api.repository.BusRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.ResponseEntity;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class BusControllerTest {
//
//    private BusRepository busRepository;
//    private BusService busService;
//    private SeatService seatService;
//    private BusPointService busPointService;
//    private BusController busController;
//
//    @BeforeEach
//    void setUp() {
//        busRepository = mock(BusRepository.class);
//        busService = mock(BusService.class);
//        seatService = mock(SeatService.class);
//        busPointService = mock(BusPointService.class);
//        busController = new BusController(busRepository, busService, seatService, busPointService);
//    }
//
//    @Test
//    void testBookSeat_BusNotFound() {
//        BookingDTO dto = new BookingDTO();
//        dto.setBusId(1L);
//        dto.setSeatCodes(List.of(101L));
//        dto.setPassengers(List.of(new BookingRequestDTO()));
//
//        when(busService.getBusById(1L)).thenReturn(null);
//
//        ResponseEntity<?> response = busController.bookSeat(dto);
//        assertEquals(400, response.getStatusCodeValue());
//        assertTrue(response.getBody().toString().contains("Bus not found"));
//    }
//
//    @Test
//    void testBookSeat_NoSeatsSelected() {
//        BookingDTO dto = new BookingDTO();
//        dto.setBusId(1L);
//        dto.setSeatCodes(Collections.emptyList());
//        dto.setPassengers(List.of(new BookingRequestDTO()));
//
//        Bus bus = new Bus();
//        bus.setId(1L);
//        when(busService.getBusById(1L)).thenReturn(bus);
//
//        ResponseEntity<?> response = busController.bookSeat(dto);
//        assertEquals(400, response.getStatusCodeValue());
//        assertTrue(response.getBody().toString().contains("No seats selected"));
//    }
//
//    @Test
//    void testBookSeat_PassengerMismatch() {
//        BookingDTO dto = new BookingDTO();
//        dto.setBusId(1L);
//        dto.setSeatCodes(List.of(101L, 102L));
//        dto.setPassengers(List.of(new BookingRequestDTO())); // only 1 passenger
//
//        Bus bus = new Bus();
//        bus.setId(1L);
//        bus.setBusName("TestBus");
//        bus.setFromCity("CityA");
//        bus.setToCity("CityB");
//        bus.setDepartureTime(LocalDateTime.now());
//        bus.setArrivalTime(LocalDateTime.now().plusHours(2));
//        bus.setDuration("2h");
//
//        when(busService.getBusById(1L)).thenReturn(bus);
//
//        Seat seat1 = new Seat();
//        seat1.setId(101L);
//        seat1.setAvailable(true);
//        seat1.setPrice(100);
//
//        Seat seat2 = new Seat();
//        seat2.setId(102L);
//        seat2.setAvailable(true);
//        seat2.setPrice(100);
//
//        when(seatService.getSeatsByIds(dto.getSeatCodes())).thenReturn(Arrays.asList(seat1, seat2));
//
//        ResponseEntity<?> response = busController.bookSeat(dto);
//        assertEquals(400, response.getStatusCodeValue());
//        assertTrue(response.getBody().toString().contains("Passenger count must match"));
//    }
//
//    @Test
//    void testBookSeat_SuccessfulBooking() {
//        BookingDTO dto = new BookingDTO();
//        dto.setBusId(1L);
//        dto.setSeatCodes(List.of(101L));
//        BookingRequestDTO passenger = new BookingRequestDTO();
//        passenger.setName("John Doe");
//        passenger.setAge(30);
//        passenger.setPhone("1234567890");
//        passenger.setState("KA");
//        dto.setPassengers(List.of(passenger));
//
//        Bus bus = new Bus();
//        bus.setId(1L);
//        bus.setBusName("TestBus");
//        bus.setFromCity("CityA");
//        bus.setToCity("CityB");
//        bus.setDepartureTime(LocalDateTime.now());
//        bus.setArrivalTime(LocalDateTime.now().plusHours(2));
//        bus.setDuration("2h");
//
//        when(busService.getBusById(1L)).thenReturn(bus);
//
//        Seat seat = new Seat();
//        seat.setId(101L);
//        seat.setAvailable(true);
//        seat.setPrice(100);
//
//        when(seatService.getSeatsByIds(dto.getSeatCodes())).thenReturn(List.of(seat));
//        when(busService.saveBooking(any(Booking.class), anyList())).thenAnswer(invocation -> invocation.getArgument(0));
//
//        ResponseEntity<?> response = busController.bookSeat(dto);
//        assertEquals(200, response.getStatusCodeValue());
//        assertTrue(response.getBody() instanceof Booking);
//    }
//}
