package com.example.api.model;

import com.example.api.enums.BusPointType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "bus_point")
public class BusPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "time", nullable = false)
    private String time;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BusPointType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_id", nullable = false)
    @JsonIgnore
    private Bus bus;

    // Default constructor
    public BusPoint() {}

    // Parameterized constructor
    public BusPoint(String name, String time, BusPointType type, Bus bus) {
        this.name = name;
        this.time = time;
        this.type = type;
        this.bus = bus;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public BusPointType getType() { return type; }
    public void setType(BusPointType type) { this.type = type; }

    public Bus getBus() { return bus; }
    public void setBus(Bus bus) { this.bus = bus; }

    @Override
    public String toString() {
        return "BusPoint [id=" + id + ", name=" + name + ", time=" + time +
               ", type=" + type + ", busId=" + (bus != null ? bus.getId() : null) + "]";
    }
}
