package com.covoiturage.main_covoiturage.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Long id;
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Column( nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
   private  String Password;
    @Enumerated(EnumType.STRING)
    private Role role = Role.ADMIN;



}
