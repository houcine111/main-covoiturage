package com.covoiturage.main_covoiturage.Services;

import com.covoiturage.main_covoiturage.Entity.Conducteur;
import com.covoiturage.main_covoiturage.Entity.Personne;
import com.covoiturage.main_covoiturage.Entity.Role;
import com.covoiturage.main_covoiturage.Entity.Trajet;
import com.covoiturage.main_covoiturage.Repository.Conducteurrepo;
import com.covoiturage.main_covoiturage.Repository.Personnerepo;
import com.covoiturage.main_covoiturage.Repository.Trajetrepo;
import com.covoiturage.main_covoiturage.dto.TrajetRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import com.covoiturage.main_covoiturage.dto.TrajetDTO;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Service

public class Personneservice {

private TrajetDTO trajetdto;
private final Personnerepo personnerepo;
    private final Conducteurrepo conducteurrepo;
    private final Trajetrepo trajetrepo;




    public Personne ajouterpersonne(HttpServletRequest request, @RequestBody Personne p){
        Object usernameObj = request.getAttribute("username");
        Optional<Personne> pp=personnerepo.findByUsername(usernameObj.toString());
        if(pp.isPresent()){
            System.out.println("Vous êtes déjà un utilisateur.");
        }


    Personne p1=new Personne();
        p1.setUsername(usernameObj.toString());
        p1.setNom(p.getNom());
        p1.setPrenom(p.getPrenom());
        p1.setEmail(p.getEmail());
        p1.setRole(p.getRole());
        p1.setNumeroTelephone(p.getNumeroTelephone());
        p1.setGenre(p.getGenre());
        personnerepo.save(p1);
        return p1;
    }
    public Conducteur ajouterconducteur(@RequestBody Conducteur con){


        Optional<Conducteur> c=conducteurrepo.findByUsername(con.getUsername());
        if(c.isPresent()){
            System.out.println("Vous êtes déjà un conducteur.");
        }
        Conducteur c1 = new Conducteur();
        c1.setUsername(con.getUsername());
        c1.setGenre(con.getGenre());
        c1.setNom(con.getNom());
        c1.setPrenom(con.getPrenom());
        c1.setEmail(con.getEmail());
        c1.setRole(Role.CONDUCTEUR);
        c1.setNumeroTelephone(con.getNumeroTelephone());
        c1.setImmatriculationPhoto(con.getImmatriculationPhoto());
        c1.setPermisPhoto(con.getPermisPhoto());
        c1.setActif(false);
     conducteurrepo.save(c1);
     return c1;

    }
    public void participerATrajet(HttpServletRequest request, Long trajetId) {
        Object usernameObj = request.getAttribute("username");
        String username = usernameObj.toString();
        Personne personne = personnerepo.findByUsername(username).orElseThrow();
        Trajet trajet = trajetrepo.findById(trajetId).orElseThrow();

        trajet.getParticipants().add(personne);
        trajetrepo.save(trajet);
    }
    /*public boolean isConducteur(HttpServletRequest request) {
        Object usernameObj = request.getAttribute("username");
        return conducteurrepo.findByUsername(usernameObj.toString()).isPresent();
    }*/

    public boolean isPersonne(HttpServletRequest request) {
        Object usernameObj = request.getAttribute("username");
        return personnerepo.findByUsername(usernameObj.toString()).isPresent();
    }
    public List<Conducteur> afficherConducteur (){

         return conducteurrepo.findAll();

    }
    public Optional<Trajet>gettrajetById(Long trajetId){
        return trajetrepo.findById(trajetId);
    }
    public List<Trajet> ajoutfilreTrajet( TrajetDTO filtre){
        List<Trajet>trajets=trajetrepo.findAll();
        return trajets.stream()
                .filter(t -> filtre.getDepart() == null || t.getDepart().equalsIgnoreCase(filtre.getDepart()))
                .filter(t -> filtre.getDestination() == null || t.getDestination().equalsIgnoreCase(filtre.getDestination()))
                .filter(t -> filtre.getDateDepart() == null || t.getDateDepart().equals(filtre.getDateDepart()))
                .filter(t -> filtre.getPrix() == null || t.getPrix() <= filtre.getPrix())
                .filter(t -> filtre.getNbplace() == null || t.getNbplace() <= filtre.getNbplace())
                .filter(t -> filtre.getFemmesOnly() == null || t.isReservedForWomen() == filtre.getFemmesOnly())
                .collect(Collectors.toList());
    }
    public Trajet fromDTO(TrajetRequestDTO dto, Conducteur conducteur) {
        Trajet trajet = new Trajet();
        trajet.setDepart(dto.getDepart());
        trajet.setDestination(dto.getDestination());

        // Convertir string vers LocalTime
        trajet.setHeureDepart(LocalTime.parse(dto.getHeureDepart()));

        trajet.setDateDepart(new Date()); // ou une date précise à parser
        trajet.setPrix(dto.getPrix());
        trajet.setStations(dto.getStation());
        trajet.setConducteur(conducteur);
        trajet.setNbplace(4); // ou une valeur par défaut / dynamique
        trajet.setParticipants(new ArrayList<>()); // vide au départ
        trajet.setReservedForWomen(false); // à mettre selon logique
        return trajet;
    }

}
