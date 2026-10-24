package com.theater_venue_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TheaterVenueServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TheaterVenueServiceApplication.class, args);
	}

}
