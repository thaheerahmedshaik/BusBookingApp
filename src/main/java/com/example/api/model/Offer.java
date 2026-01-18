package com.example.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "offers")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String couponCode;
    private int discountAmount;
    private String validTill;
    private String category; // BUS / TRAIN / ALL

    private String bgColor; // for UI styling

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public int getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(int discountAmount) {
		this.discountAmount = discountAmount;
	}

	public String getValidTill() {
		return validTill;
	}

	public void setValidTill(String validTill) {
		this.validTill = validTill;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public Offer(Long id, String title, String description, String couponCode, int discountAmount, String validTill,
			String category, String bgColor) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.couponCode = couponCode;
		this.discountAmount = discountAmount;
		this.validTill = validTill;
		this.category = category;
		this.bgColor = bgColor;
	}

    // getters & setters
    public Offer() {
    	
    }

	@Override
	public String toString() {
		return "Offer [id=" + id + ", title=" + title + ", description=" + description + ", couponCode=" + couponCode
				+ ", discountAmount=" + discountAmount + ", validTill=" + validTill + ", category=" + category
				+ ", bgColor=" + bgColor + "]";
	}
    
}
