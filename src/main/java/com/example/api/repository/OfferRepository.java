package com.example.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.api.model.Offer;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findByCategoryOrCategory(String cat1, String cat2);
}
