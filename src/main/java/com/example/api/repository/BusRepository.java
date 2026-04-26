package com.example.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    @Query("SELECT b FROM Bus b WHERE b.fromCity = :from AND b.toCity = :to AND DATE(b.departureTime) = :date")
    List<Bus> findByFromCityAndToCityAndDate(String from, String to, LocalDate date);
    
    
    // ✅ OPTIONAL: Sorting (Price Low → High)
    @Query("""
        SELECT b FROM Bus b
        WHERE b.fromCity = :from AND b.toCity = :to
        ORDER BY b.price ASC
    """)
    List<Bus> findByRouteOrderByPrice(@Param("from") String from,
                                      @Param("to") String to);


    // ✅ OPTIONAL: Sorting by rating
    @Query("""
        SELECT b FROM Bus b
        WHERE b.fromCity = :from AND b.toCity = :to
        ORDER BY b.rating DESC
    """)
    List<Bus> findByRouteOrderByRating(@Param("from") String from,
                                       @Param("to") String to);

    

    @Query("""
        SELECT b FROM Bus b
        WHERE LOWER(b.fromCity) = LOWER(:from)
        AND LOWER(b.toCity) = LOWER(:to)

        AND (:date IS NULL OR b.date = :date)

        AND (:seatType IS NULL OR LOWER(b.seatType) = LOWER(:seatType))

        AND (:minPrice IS NULL OR b.price >= :minPrice)
        AND (:maxPrice IS NULL OR b.price <= :maxPrice)

        AND (:minRating IS NULL OR CAST(b.rating as double) >= :minRating)

       AND (
        :amenities IS NULL
        OR LOWER(REPLACE(b.amenities, ' ', '')) 
           LIKE LOWER(CONCAT('%', REPLACE(:amenities, ' ', ''), '%'))
    )
""")
    List<Bus> filterBuses(
            @Param("from") String from,
            @Param("to") String to,
            @Param("date") LocalDate date,
            @Param("seatType") String seatType,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("minRating") Double minRating,
            @Param("amenities") String amenities
    );
}
