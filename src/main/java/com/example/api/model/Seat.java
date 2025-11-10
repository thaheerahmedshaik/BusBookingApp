package com.example.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;
    private String type;
    private String deck;
    private double price;
    private boolean available;

    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDeck() {
		return deck;
	}

	public void setDeck(String deck) {
		this.deck = deck;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public Bus getBus() {
		return bus;
	}

	public void setBus(Bus bus) {
		this.bus = bus;
	}

	public Seat(Long id, String number, String type, String deck, double price, boolean available, Bus bus) {
		super();
		this.id = id;
		this.number = number;
		this.type = type;
		this.deck = deck;
		this.price = price;
		this.available = available;
		this.bus = bus;
	}

	@Override
	public String toString() {
		return "Seat [id=" + id + ", number=" + number + ", type=" + type + ", deck=" + deck + ", price=" + price
				+ ", available=" + available + ", bus=" + bus + "]";
	}
    public Seat() {}
    
}