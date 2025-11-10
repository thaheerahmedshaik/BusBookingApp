package com.example.api.repository;

import com.example.api.model.BusPoint;
import com.example.api.enums.BusPointType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BusPointRepository extends JpaRepository<BusPoint, Long> {

    // Get all points for a bus by type (BOARDING or DROPPING)
    List<BusPoint> findByBusIdAndType(Long busId, BusPointType type);

    // Get all points for a bus
    List<BusPoint> findByBusId(Long busId);
}
