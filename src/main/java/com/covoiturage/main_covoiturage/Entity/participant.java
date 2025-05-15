package com.covoiturage.main_covoiturage.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class participant {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Trajet trajet;

    private String nom;
    private String prenom;
    private String numerotel;
    private String email;
    private String telephone;
}
