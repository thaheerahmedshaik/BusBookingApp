package com.example.api.model;

import java.util.List;

// Main DTO for booking seats
public class BookingDTO {

    private Long busId;                   // Bus ID
    private List<Long> seatIds;           // Selected seat IDs
    private String boardingPoint;         // Boarding point
    private String droppingPoint;         // Dropping point
    private List<BookingRequestDTO> passengers; // Passenger details

    // Getters and setters
    public Long getBusId() { return busId; }
    public void setBusId(Long busId) { this.busId = busId; }

    public List<Long> getSeatIds() { return seatIds; }
    public void setSeatIds(List<Long> seatIds) { this.seatIds = seatIds; }

    public String getBoardingPoint() { return boardingPoint; }
    public void setBoardingPoint(String boardingPoint) { this.boardingPoint = boardingPoint; }

    public String getDroppingPoint() { return droppingPoint; }
    public void setDroppingPoint(String droppingPoint) { this.droppingPoint = droppingPoint; }

    public List<BookingRequestDTO> getPassengers() { return passengers; }
    public void setPassengers(List<BookingRequestDTO> passengers) { this.passengers = passengers; }
}
