package com.covoiturage.main_covoiturage.Config;

import org.springframework.context.annotation.Configuration;
import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
@Configuration
public class Googlemapsconfig {
    @Value("${google.maps.api.key}")
    private String apiKey;

    @Bean
    public GeoApiContext geoApiContext() {
        return new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
    }
}
