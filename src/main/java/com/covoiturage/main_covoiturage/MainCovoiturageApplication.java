package com.covoiturage.main_covoiturage;

import com.covoiturage.main_covoiturage.Services.GoogleMapsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainCovoiturageApplication {
	@Autowired
	private GoogleMapsService googleMapsService;

	public static void main(String[] args) {
		SpringApplication.run(MainCovoiturageApplication.class, args);
		System.out.println("hello world");

	}

}
