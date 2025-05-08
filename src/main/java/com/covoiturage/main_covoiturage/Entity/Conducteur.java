package com.covoiturage.main_covoiturage.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Conducteur {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Long id;
    private String nom;
    private String prenom;
    @Column( unique = true, nullable = false)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;




    private String genre;

    private String numeroTelephone;

    private String permisPhoto;


    private String immatriculationPhoto;
    @Enumerated(EnumType.STRING)
    private Role role=Role.CONDUCTEUR;
    @JsonManagedReference
    @OneToMany(mappedBy = "conducteur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Trajet> trajets;

}
