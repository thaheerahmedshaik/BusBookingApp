package com.example.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bus_id", nullable = false)
    private Long busId;

    @Column(name = "bus_name", nullable = false)
    private String busName;

    @Column(name = "from_city", nullable = false)
    private String fromCity;

    @Column(name = "to_city", nullable = false)
    private String toCity;

    @Column(name = "departure_time", nullable = false)
    private String departureTime;

    @Column(name = "arrival_time", nullable = false)
    private String arrivalTime;

    @Column(name = "duration", nullable = false)
    private String duration;

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "boarding_point", nullable = false)
    private String boardingPoint;

    @Column(name = "dropping_point", nullable = false)
    private String droppingPoint;

    @Column(name = "travel_date", nullable = false)
    private LocalDate travelDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "booking_id")
    @JsonManagedReference
    private List<BookingRequest> passengers = new ArrayList<>();

    public Booking() {}

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBusId() { return busId; }
    public void setBusId(Long busId) { this.busId = busId; }

    public String getBusName() { return busName; }
    public void setBusName(String busName) { this.busName = busName; }

    public String getFromCity() { return fromCity; }
    public void setFromCity(String fromCity) { this.fromCity = fromCity; }

    public String getToCity() { return toCity; }
    public void setToCity(String toCity) { this.toCity = toCity; }

    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getBoardingPoint() { return boardingPoint; }
    public void setBoardingPoint(String boardingPoint) { this.boardingPoint = boardingPoint; }

    public String getDroppingPoint() { return droppingPoint; }
    public void setDroppingPoint(String droppingPoint) { this.droppingPoint = droppingPoint; }

    public LocalDate getTravelDate() { return travelDate; }
    public void setTravelDate(LocalDate travelDate) { this.travelDate = travelDate; }

    public List<BookingRequest> getPassengers() { return passengers; }
    public void setPassengers(List<BookingRequest> passengers) { this.passengers = passengers; }

    @Override
    public String toString() {
        return "Booking [id=" + id + ", busId=" + busId + ", busName=" + busName + ", fromCity=" + fromCity +
                ", toCity=" + toCity + ", departureTime=" + departureTime + ", arrivalTime=" + arrivalTime +
                ", duration=" + duration + ", totalAmount=" + totalAmount + ", price=" + price +
                ", boardingPoint=" + boardingPoint + ", droppingPoint=" + droppingPoint +
                ", travelDate=" + travelDate + ", passengers=" + passengers + "]";
    }
}
