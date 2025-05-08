package com.covoiturage.main_covoiturage.dto;

import com.covoiturage.main_covoiturage.Entity.Trajet;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
public class TrajetDTO {
    private String depart;
    private String destination;
    private LocalTime heureDepart;
    private Double prix;
    private Boolean femmesOnly;
    public TrajetDTO(Trajet trajet) {
        this.depart = trajet.getDepart();
        this.destination = trajet.getDestination();
        this.heureDepart = trajet.getHeureDepart();
        this.prix=trajet.getPrix();
        this.femmesOnly=trajet.isReservedForWomen();
    }
}
