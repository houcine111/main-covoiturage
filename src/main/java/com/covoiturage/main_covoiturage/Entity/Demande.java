package com.covoiturage.main_covoiturage.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String numerotel;
    @Enumerated(EnumType.STRING)
    private Status status=Status.EN_ATTENTE;
    @ManyToOne
    @JoinColumn(name = "trajet_id")
    private Trajet trajet;



}
