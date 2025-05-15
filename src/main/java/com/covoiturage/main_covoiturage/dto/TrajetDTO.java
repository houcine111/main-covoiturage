package com.covoiturage.main_covoiturage.dto;

import com.covoiturage.main_covoiturage.Entity.Trajet;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;

@Data
@NoArgsConstructor
public class TrajetDTO {
    private String depart;
    private String destination;
    private LocalTime heureDepart;
    private Date dateDepart;

    private Double prix;

    private Boolean femmesOnly;
    private Integer nbplace;
    public TrajetDTO(Trajet trajet) {
        this.depart = trajet.getDepart();
        this.destination = trajet.getDestination();
        this.dateDepart = trajet.getDateDepart();
        this.heureDepart = trajet.getHeureDepart();
        this.prix=trajet.getPrix();
        this.femmesOnly=trajet.isReservedForWomen();
        this.nbplace=trajet.getNbplace();
    }
}
