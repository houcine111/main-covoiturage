package com.covoiturage.main_covoiturage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrajetRequestDTO {
    private String username;
    private String depart;
    private String destination;
    private String datedepart;
    private String heureDepart;
    private double prix;
    private List<String> station;
    private int nbplace;
}
