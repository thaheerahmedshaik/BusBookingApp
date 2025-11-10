package com.example.api.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "bus")
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bus_name")
    private String busName;

    @Column(name = "from_city")
    private String fromCity;

    @Column(name = "to_city")
    private String toCity;

    @Column(name = "departure_time")
    private String departureTime;

    @Column(name = "arrival_time")
    private String arrivalTime;

    @Column(name = "duration")
    private String duration;

    @Column(name = "price")
    private double price;

    @Column(name = "date")
    private LocalDate date;

    
    @Column(name="seat_type")
    private String seatType;
    
    @Column(name="rating")
    private String rating;
    
    @Column(name="amenities")
    private String amenities;
    
    // Default constructor (required by Hibernate)
    public Bus() {
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBusName() {
		return busName;
	}

	public void setBusName(String busName) {
		this.busName = busName;
	}

	public String getFromCity() {
		return fromCity;
	}

	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}

	public String getToCity() {
		return toCity;
	}

	public void setToCity(String toCity) {
		this.toCity = toCity;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getSeatType() {
		return seatType;
	}

	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getAmenities() {
		return amenities;
	}

	public void setAmenities(String amenities) {
		this.amenities = amenities;
	}

	public Bus(Long id, String busName, String fromCity, String toCity, String departureTime, String arrivalTime,
			String duration, double price, LocalDate date, String seatType, String rating, String amenities) {
		super();
		this.id = id;
		this.busName = busName;
		this.fromCity = fromCity;
		this.toCity = toCity;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.duration = duration;
		this.price = price;
		this.date = date;
		this.seatType = seatType;
		this.rating = rating;
		this.amenities = amenities;
	}

	@Override
	public String toString() {
		return "Bus [id=" + id + ", busName=" + busName + ", fromCity=" + fromCity + ", toCity=" + toCity
				+ ", departureTime=" + departureTime + ", arrivalTime=" + arrivalTime + ", duration=" + duration
				+ ", price=" + price + ", date=" + date + ", seatType=" + seatType + ", rating=" + rating
				+ ", amenities=" + amenities + "]";
	}

   
}
