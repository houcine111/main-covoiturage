package com.covoiturage.main_covoiturage.Services;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class PersonneDataService {

    private final WebClient webClient;

    // Use constructor injection
    public PersonneDataService(WebClient personneWebClient) {
        this.webClient = personneWebClient;
    }

    public Map<String, Object> fetchPersonneDataFrom8085(String username) {
        try {
            return webClient.get()
                    .uri("/auth/personne/{username}", username)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (Exception e) {
            Map<String, Object> fallback = new HashMap<>();
            fallback.put("error", "Failed to fetch data from authentication service");
            return fallback;
        }
    }
}