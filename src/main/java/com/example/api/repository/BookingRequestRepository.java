package com.example.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.api.model.BookingRequest;
@Repository
public interface BookingRequestRepository extends JpaRepository<BookingRequest, Long> 

{}