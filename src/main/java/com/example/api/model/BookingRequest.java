package com.example.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
public class BookingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;
    private String phone;
    private String state;

    // Many passengers belong to one booking
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false) // foreign key in BookingRequest table
    @JsonBackReference
    private Booking booking;

    // Optional: store seatId if needed
    private Long seatId;

    // Default constructor
    public BookingRequest() {}

    // Constructor without booking
    public BookingRequest(String name, int age, String phone, String state) {
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.state = state;
    }

    // Constructor with booking
    public BookingRequest(String name, int age, String phone, String state, Booking booking) {
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.state = state;
        this.booking = booking;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }

    public Long getSeatId() { return seatId; }
    public void setSeatId(Long seatId) { this.seatId = seatId; }

    @Override
    public String toString() {
        return "BookingRequest [name=" + name + ", age=" + age + ", phone=" + phone + ", state=" + state + ", seatId=" + seatId + "]";
    }
}
