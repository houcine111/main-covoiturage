package com.covoiturage.main_covoiturage.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data

public class Personne {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Long id;
    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "prenom" , nullable = false)

    private String prenom;

    @Column( unique = true, nullable = false)
    private String username;
    @Column(  name = "email", unique = true, nullable = false)
    private String email;



    @Column(name = "genre", nullable = false)

    private String genre;

    @Column(name = "numerotelephone", nullable = false)

    private String numeroTelephone;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role=Role.USER;


}
