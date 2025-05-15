package com.covoiturage.main_covoiturage.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Trajet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String depart;
    private String destination;
    private LocalTime heureDepart;
    private Date dateDepart;
    private double prix;

    @ElementCollection
    private List<String> stations;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "conducteur_id", nullable = false)
    private Conducteur conducteur;
    private int nbplace;
    @ManyToMany

    private List<Personne> participants;
    private boolean reservedForWomen;


}
