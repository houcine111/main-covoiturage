package com.covoiturage.main_covoiturage.Controller;

import com.covoiturage.main_covoiturage.Entity.Conducteur;
import com.covoiturage.main_covoiturage.Entity.Personne;
import com.covoiturage.main_covoiturage.Entity.Role;
import com.covoiturage.main_covoiturage.Entity.Trajet;
import com.covoiturage.main_covoiturage.Repository.Conducteurrepo;
import com.covoiturage.main_covoiturage.Repository.Personnerepo;
import com.covoiturage.main_covoiturage.Repository.Trajetrepo;
import com.covoiturage.main_covoiturage.Services.Personneservice;
import com.covoiturage.main_covoiturage.dto.TrajetDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class CovoitController {
    private final Personneservice personneservice;

    private final Conducteurrepo conducteurrepo;
    private final Trajetrepo trajetrepo;
    private final Personnerepo personnerepo;


    @Autowired
    public CovoitController(Personneservice personneservice, Conducteurrepo conducteurrepo, Trajetrepo trajetrepo, Personnerepo personnerepo) {
        this.personneservice = personneservice;
        this.conducteurrepo = conducteurrepo;
        this.trajetrepo = trajetrepo;
        this.personnerepo = personnerepo;
    }
    @GetMapping
    public String index( HttpServletRequest request) {
        Object usernameObj = request.getAttribute("username");

        if (usernameObj != null) {
            String username = usernameObj.toString();
            return "Hello " + username;
        } else {
            return "Hello, guest";
        }
    }
    @PostMapping("/completer-profil")
    public ResponseEntity<?> completerProfil(HttpServletRequest request, @RequestBody Personne personne) {
        if (personneservice.isPersonne(request)) {
            return ResponseEntity.badRequest().body("Vous êtes déjà enregistré en tant que personne.");
        }

        Personne nouvellePersonne = personneservice.ajouterpersonne(request, personne);
        return ResponseEntity.ok(nouvellePersonne);
    }
    @PostMapping("/devenir-conducteur")
    public ResponseEntity<?> devenirConducteur(HttpServletRequest request,@RequestBody Conducteur conduct) {
        if (!personneservice.isConducteur(request)) {
            Conducteur conducteur = personneservice.ajouterconducteur(request,conduct);
            return ResponseEntity.ok("Vous êtes maintenant conducteur !"+conducteur);
        } else {
            return ResponseEntity.badRequest().body("Vous êtes déjà conducteur !");
        }
    }


    @PostMapping("/participer-trajet")
    public ResponseEntity<?> participerTrajet(HttpServletRequest request, @RequestParam Long trajetId) {
        if (!personneservice.isPersonne(request) ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous devez compléter votre profil pour participer.");

        }
            Object objusername=request.getAttribute("username");
            String username=objusername.toString();
            Optional<Personne> personne=personnerepo.findByUsername(username);

        Trajet trajet = trajetrepo.findById(trajetId).orElseThrow();
        if(trajet.isReservedForWomen() && !personne.get().getGenre().equals("femme")){
            throw new RuntimeException("Ce trajet est réservé uniquement pour les femmes.");

        }
            int nbparticpanttrajet=trajet.getParticipants().size();

            if(nbparticpanttrajet>=3) {
                throw new RuntimeException("Désolé, ce trajet est déjà complet.");
            }
        personneservice.participerATrajet(request, trajetId);
        return ResponseEntity.ok("Participation enregistrée !");



    }
    @PostMapping("/ajoutertrajet")
    public ResponseEntity<?> ajoutertrajet(@RequestBody Trajet trajet) {
        Long idcondu = trajet.getConducteur().getId();
        Optional<Trajet> existing = trajetrepo.findBydepart(
                idcondu, trajet.getDepart(), trajet.getHeureDepart());

        Optional<Conducteur> conducteurOpt = conducteurrepo.findById(idcondu.intValue());
        if (conducteurOpt.isPresent()) {
            Conducteur conducteur = conducteurOpt.get();
            if(conducteur.getRole()== Role.CONDUCTEUR){
                if(existing.isPresent()){
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("Vous avez déjà un trajet au même endroit à la même heure.");

                }
                if(conducteur.getGenre().equals("femme")){
                    trajet.setReservedForWomen(trajet.isReservedForWomen());
                }else {
                    trajet.setReservedForWomen(false);
                }
            trajet.setConducteur(conducteur); // Très important !

            Trajet saved = trajetrepo.save(trajet);
            return ResponseEntity.ok(saved);
        }else {
                System.out.println("vous netes pas un conducteur");
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conducteur introuvable.");
    }
    @PostMapping("/chercher_trajet")
    public ResponseEntity<?> chercherTrajet(@RequestBody TrajetDTO trajet) {
        List<Trajet> resultats=personneservice.ajoutfilreTrajet(trajet);
        return ResponseEntity.ok(resultats);
    }
    @GetMapping("/user/{id}")
    public List<Trajet>afficher_trajet_personne(@PathVariable Long id){
        Optional<Personne> personne=personnerepo.findById(id);

        if(personne.isEmpty()){
        return  Collections.emptyList();
        }
        return trajetrepo.findByParticipants_Id(id);


    }
    @GetMapping("/driver/{id}")
    public List<Trajet>afficher_trajet(@PathVariable Long id){
        Optional<Conducteur> idcond=conducteurrepo.findById(id.intValue());

        if(idcond.isEmpty()){
            System.out.println("vous etes pas un conducteur");
        }
        List<Trajet>trajets=idcond.get().getTrajets();
        return trajets;
    }
    @PostMapping("/driver/{id}/update_trajet")
    public ResponseEntity<?>modifiertrajet(@RequestBody Trajet trajet,@PathVariable Long id) {
        Optional<Conducteur> idcond=conducteurrepo.findById(id.intValue());
        Optional<Trajet>trj=trajetrepo.findById(trajet.getId());
        if (idcond.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conducteur introuvable.");
        }

        if (trj.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trajet introuvable.");
        }
    Conducteur conducteur=idcond.get();
        Trajet trajet1=trj.get();
        if(!trajet1.getConducteur().getId().equals(conducteur.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous n'êtes pas autorisé à modifier ce trajet.");

        }
        // Mise à jour des champs
        trajet1.setDepart(trajet.getDepart());
        trajet1.setDestination(trajet.getDestination());
        trajet1.setHeureDepart(trajet.getHeureDepart());
        trajet1.setPrix(trajet.getPrix());
        trajet1.setStations(trajet.getStations());

        if (conducteur.getGenre().equalsIgnoreCase("femme")) {
            trajet1.setReservedForWomen(trajet.isReservedForWomen());
        } else {
            trajet1.setReservedForWomen(false);
        }

          trajetrepo.save(trajet1);
           return ResponseEntity.ok(trajet1);



    }
@PostMapping("/driver/{id}/supprimer_trajet")
    public ResponseEntity<?>supprimertrajet(@RequestBody Trajet trajet ,@PathVariable Long id) {
        Optional<Conducteur> idcond=conducteurrepo.findById(id.intValue());
        Optional<Trajet>trj=trajetrepo.findById(trajet.getId());
        if(idcond.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conducteur introuvable.");

        }
        if (trj.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("trajet introuvable.");

        }
        if(!trj.get().getConducteur().getId().equals(idcond.get().getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("impossible de supprimer le trajet .");

        }
        trajetrepo.delete(trajet);
        return ResponseEntity.ok("Trajet supprimé avec succès.");

    }

}
