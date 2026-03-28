package com.example.api.model;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

// Main DTO for booking seats
public class BookingDTO {

	 @NotNull(message = "Bus ID is required")
	    private Long busId;

	    @NotEmpty(message = "At least one seat must be selected")
	    private List<Long> seatIds;

	    private String boardingPoint;
	    private String droppingPoint;

	    @NotEmpty(message = "Passenger details are required")
	    private List<BookingRequestDTO> passengers; 

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
