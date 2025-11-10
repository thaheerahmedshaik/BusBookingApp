package com.example.api.service;

import com.example.api.enums.BusPointType;
import com.example.api.model.BusPoint;

import com.example.api.repository.BusPointRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusPointService {

    private final BusPointRepository busPointRepository;

    public BusPointService(BusPointRepository busPointRepository) {
        this.busPointRepository = busPointRepository;
    }

    // Get boarding points for a bus
    public List<BusPoint> getBoardingPoints(Long busId) {
        return busPointRepository.findByBusIdAndType(busId, BusPointType.BOARDING);
    }

    // Get dropping points for a bus
    public List<BusPoint> getDroppingPoints(Long busId) {
        return busPointRepository.findByBusIdAndType(busId, BusPointType.DROPPING);
    }

    // Add a new point
    public BusPoint addPoint(BusPoint point) {
        return busPointRepository.save(point);
    }

    // Get all points for a bus
    public List<BusPoint> getAllPoints(Long busId) {
        return busPointRepository.findByBusId(busId);
    }
}
