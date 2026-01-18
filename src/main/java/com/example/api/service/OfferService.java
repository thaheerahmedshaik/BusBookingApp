package com.example.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.api.model.Offer;
import com.example.api.repository.OfferRepository;

@Service
public class OfferService {

    @Autowired
    private OfferRepository offerRepository;

    public List<Offer> getBusOffers() {
        return offerRepository.findByCategoryOrCategory("BUS", "ALL");
    }
}
