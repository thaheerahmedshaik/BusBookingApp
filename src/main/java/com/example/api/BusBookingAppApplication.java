package com.example.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication

public class BusBookingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusBookingAppApplication.class, args);
		   BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	        String rawPassword = "ahmed";
	        String encodedPassword = encoder.encode(rawPassword);
	        System.out.println("BCrypt hash for 'ahmed': " + encodedPassword);
	}

}
