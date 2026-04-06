package com.example.api.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class BookingDTO {

    @NotNull(message = "Bus ID is required")
    private Long busId;

    @NotEmpty(message = "At least one seat must be selected")
    private List<String> seatCodes;   // seat codes like "A1", "C2"

    private String boardingPoint;
    private String droppingPoint;

    @NotEmpty(message = "Passenger details are required")
    private List<BookingRequestDTO> passengers;

    // Getters and setters
    public Long getBusId() { return busId; }
    public void setBusId(Long busId) { this.busId = busId; }

    public List<String> getSeatCodes() { return seatCodes; }
    public void setSeatCodes(List<String> seatCodes) { this.seatCodes = seatCodes; }

    public String getBoardingPoint() { return boardingPoint; }
    public void setBoardingPoint(String boardingPoint) { this.boardingPoint = boardingPoint; }

    public String getDroppingPoint() { return droppingPoint; }
    public void setDroppingPoint(String droppingPoint) { this.droppingPoint = droppingPoint; }

    public List<BookingRequestDTO> getPassengers() { return passengers; }
    public void setPassengers(List<BookingRequestDTO> passengers) { this.passengers = passengers; }
}
