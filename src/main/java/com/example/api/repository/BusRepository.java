package com.example.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.api.model.Bus;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {

    // Fetch all distinct source cities
    @Query("SELECT DISTINCT b.fromCity FROM Bus b")
    List<String> findDistinctFromCities();

    // Fetch all distinct destination cities
    @Query("SELECT DISTINCT b.toCity FROM Bus b")
    List<String> findDistinctToCities();

    // Search without date
    List<Bus> findByFromCityAndToCity(String fromCity, String toCity);

    // Search with date
    List<Bus> findByFromCityAndToCityAndDate(String from, String to, LocalDate date);
}
