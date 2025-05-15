package com.covoiturage.main_covoiturage.Controller;

import com.covoiturage.main_covoiturage.Entity.*;
import com.covoiturage.main_covoiturage.Repository.*;
import com.covoiturage.main_covoiturage.Services.PersonneDataService;
import com.covoiturage.main_covoiturage.Services.Personneservice;
import com.covoiturage.main_covoiturage.dto.TrajetDTO;
import com.covoiturage.main_covoiturage.dto.TrajetRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

@RestController
public class CovoitController {
    private final Personneservice personneservice;

    private final Conducteurrepo conducteurrepo;
    private final Trajetrepo trajetrepo;
    private final Personnerepo personnerepo;
    private final Demanderepo  demanderepo;
    private final participantrepo participantrepo;
    private final PersonneDataService personneDataService;


    @Autowired
    public CovoitController(Personneservice personneservice, Conducteurrepo conducteurrepo, Trajetrepo trajetrepo, Personnerepo personnerepo,Demanderepo demanderepo,participantrepo participantrepo,PersonneDataService personneDataService) {
        this.personneservice = personneservice;
        this.conducteurrepo = conducteurrepo;
        this.trajetrepo = trajetrepo;
        this.personnerepo = personnerepo;
        this.demanderepo=demanderepo;
        this.participantrepo = participantrepo;
        this.personneDataService = personneDataService;
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
    @GetMapping("/driver/{username}")
    public ResponseEntity<?> getDriverByUsername(@PathVariable String username) {
        Optional<Conducteur> conducteurOpt = conducteurrepo.findByUsername(username);

        if (conducteurOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Driver not found");
        }
        List<Trajet> trajets = trajetrepo.findByConducteurId(conducteurOpt.get().getId());
        if(trajets.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No trips found for this driver");

        }
        return ResponseEntity.ok(trajets);
    }
    @PostMapping("/ajoutertrajet")
    public ResponseEntity<?> ajoutertrajet(@RequestBody Trajet trajet) {
        Long idcondu = trajet.getConducteur().getId();
        Optional<Trajet> existing = trajetrepo.findBydepart(
                idcondu, trajet.getDepart(), trajet.getHeureDepart());

        Optional<Conducteur> conducteurOpt = conducteurrepo.findById(idcondu);
        if (conducteurOpt.isPresent()) {
            Conducteur conducteur = conducteurOpt.get();
            if(conducteur.getRole()== Role.CONDUCTEUR && conducteur.getActif().equals(true)){
                if(existing.isPresent()){
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("Vous avez déjà un trajet au même endroit à la même heure.");

                }
                if(conducteur.getGenre().equals("femme")){
                    trajet.setReservedForWomen(trajet.isReservedForWomen());
                }else {
                    trajet.setReservedForWomen(false);
                }
            trajet.setConducteur(conducteur);
                // Très important !

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
    /*@GetMapping("/driver/{id}")
    public List<Trajet>afficher_trajet(@PathVariable Long id){
        Optional<Conducteur> idcond=conducteurrepo.findById(id.intValue());

        if(idcond.isEmpty()){
            System.out.println("vous etes pas un conducteur");
        }
        List<Trajet>trajets=idcond.get().getTrajets();
        return trajets;
    }*/
    @PostMapping("/driver/{id}/update_trajet")
    public ResponseEntity<?>modifiertrajet(@RequestBody Trajet trajet,@PathVariable Long id) {
        Optional<Conducteur> idcond=conducteurrepo.findById(id);
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
    @DeleteMapping("/driver/{username}/trajet/{trajetId}")
    public ResponseEntity<?>supprimertrajet(@RequestBody Trajet trajet ,@PathVariable String username) {
        Optional<Conducteur> idcond=conducteurrepo.findByUsername(username);

        Optional<Trajet>trj=trajetrepo.findById(trajet.getId());
        if(idcond.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conducteur introuvable.");

        }
        Long idconducteur=idcond.get().getId();
        if (trj.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("trajet introuvable.");

        }
        if(!trj.get().getConducteur().getId().equals(idconducteur)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("impossible de supprimer le trajet .");

        }
        trajetrepo.delete(trajet);
        return ResponseEntity.ok("Trajet supprimé avec succès.");

    }
    @PostMapping("/{username}/ajouter_trajet")
    public ResponseEntity<?> ajouter_trajet( @PathVariable String username,@RequestBody TrajetRequestDTO dto) {

        Optional<Conducteur> conducteurOpt = conducteurrepo.findByUsername(username);
        if (conducteurOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conducteur introuvable");
        }

        Conducteur conducteur = conducteurOpt.get();

        if (conducteur.getRole() != Role.CONDUCTEUR || !conducteur.getActif()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Utilisateur non autorisé");
        }

        Optional<Trajet> existing = trajetrepo.findBydepart(
                conducteur.getId(), dto.getDepart(), LocalTime.parse(dto.getHeureDepart()));

        if (existing.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Trajet déjà existant à la même heure et au même départ");
        }

        Trajet trajet = personneservice.fromDTO(dto, conducteur);

        if (conducteur.getGenre().equalsIgnoreCase("femme")) {
            trajet.setReservedForWomen(true);
        }

        Trajet saved = trajetrepo.save(trajet);
        return ResponseEntity.ok(saved);
    }
    @GetMapping("/trajets")
    public ResponseEntity<?> afficher_trajet() {
        List<Trajet> trajets = trajetrepo.findAllTrajetsWithStationsAndConducteur();

        if (trajets.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trajet introuvable.");
        }

        List<Map<String, Object>> response = trajets.stream().map(t -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id",t.getId());
            map.put("depart", t.getDepart());
            map.put("destination", t.getDestination());
            map.put("prix", t.getPrix());
            map.put("heureDepart", t.getHeureDepart());
            map.put("dateDepart", t.getDateDepart());
            map.put("nbplace", t.getNbplace());
            map.put("stations", t.getStations());
            map.put("username", t.getConducteur().getUsername());
            return map;
        }).toList();

        return ResponseEntity.ok(response);
    }
    @PostMapping("/demande/add")
    public ResponseEntity<?> createDemande(@RequestBody Demande demande) {
        Optional<Trajet> trajetOpt = trajetrepo.findById(demande.getTrajet().getId());
        if (trajetOpt.isPresent()) {
            demande.setTrajet(trajetOpt.get());
            demanderepo.save(demande);
            return ResponseEntity.ok("Demande saved");
        }
        return ResponseEntity.badRequest().body("Trajet not found");
    }
    @GetMapping("/driver/{username}/demandes")
    public List<Demande> getDemandesForConducteur(@PathVariable String  username) {
        return demanderepo.findByTrajetConducteurUsername(username);
    }

    @PutMapping("/driver/{username}/demandes/{id}/status")
    public ResponseEntity<?> updateDemandeStatus(@PathVariable String username,@PathVariable Long id, @RequestParam String status) {
        Optional<Demande> demandeOpt = demanderepo.findById(id);
        if (demandeOpt.isPresent()) {
            Demande demande = demandeOpt.get();
            Status newStatus = Status.valueOf(status.toUpperCase());

            demande.setStatus(newStatus);
            demanderepo.save(demande);

            if (newStatus == Status.ACCEPTEE) {
                Trajet trajet = demande.getTrajet();
                if (trajet.getNbplace() > 0) {
                    // Create and save participant
                    participant prt = new participant();
                    prt.setNom(demande.getNom());
                    prt.setPrenom(demande.getPrenom());
                    prt.setEmail(demande.getEmail());
                    prt.setNumerotel(demande.getNumerotel());
                    prt.setTrajet(trajet);
                    participantrepo.save(prt);

                    // Decrement number of places
                    trajet.setNbplace(trajet.getNbplace() - 1);
                    trajetrepo.save(trajet);
                    demanderepo.delete(demande);
                } else if (newStatus == Status.REFUSEE) {

                    demanderepo.delete(demande);

                }

                else {
                    return ResponseEntity.badRequest().body("No available places left");
                }
            }

            return ResponseEntity.ok("Demande updated");
        }

        return ResponseEntity.notFound().build();
    }
    @GetMapping("/driver/{username}/participants")
    public ResponseEntity<List<participant>> getParticipantsByConducteur(@PathVariable String username) {
        List<participant> participants = participantrepo.findByTrajetConducteurUsername(username);
        return ResponseEntity.ok(participants);
    }
    @GetMapping("/conducteur/{username}")
    public ResponseEntity<?> isConducteur(@PathVariable String username) {
        Optional<Conducteur>conducteur=conducteurrepo.findByUsername(username);
        if(conducteur.isEmpty() ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conducteur not found");
        }
        Conducteur conducteur1 = conducteur.get();
        Map<String, Object> response = new HashMap<>();
        response.put("isConducteur", true);
        response.put("isActif", conducteur1.getActif());
        response.put("conducteurId", conducteur1.getId());

        return ResponseEntity.ok(response);
    }
    @PostMapping(value = "/devenir-conducteur", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> verifierConducteur(
                                                @RequestParam("nom") String nom,
                                                @RequestParam("prenom") String prenom,
                                                @RequestParam("email") String email,
                                                @RequestParam("username") String username,
                                                @RequestParam("genre") String genre,
                                                @RequestParam("role") String roleStr,
                                                @RequestParam("numeroTelephone") String numeroTelephone,
                                                @RequestParam("permisPhoto") MultipartFile permisPhoto,
                                                @RequestParam("immatriculationPhoto") MultipartFile immatriculationPhoto) {
        Conducteur con = new Conducteur();
        Role role;
        try {
            role = Role.valueOf(roleStr);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid role value");
        }
        con.setUsername(username);
        con.setNom(nom);
        con.setPrenom(prenom);
        con.setEmail(email);
        con.setGenre(genre);
        con.setRole(role);
        con.setNumeroTelephone(numeroTelephone);
        try {
            con.setPermisPhoto(permisPhoto.getBytes());
            con.setImmatriculationPhoto(immatriculationPhoto.getBytes());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors du traitement des fichiers");
        }

        Conducteur saved = personneservice.ajouterconducteur( con);
        return ResponseEntity.ok(saved);
    }
    @GetMapping("/get-personne/{username}")
    public ResponseEntity<?>getpersonne(@PathVariable String username) {

        Optional<Personne>personneOpt = personnerepo.findByUsername(username);
        if(personneOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Personne not found");
        }
        Map<String, Object> remoteData = personneDataService.fetchPersonneDataFrom8085(username);
        if (remoteData.containsKey("error")) {
            // Fallback response if 8085 is unavailable
            Map<String, Object> localResponse = new HashMap<>();
            localResponse.put("localData", Map.of(
                    "id", personneOpt.get().getId(),
                    "role", personneOpt.get().getRole()
            ));
            localResponse.put("warning", remoteData.get("error"));
            return ResponseEntity.ok(localResponse);
        }
        // 3. Combine responses
        Map<String, Object> combinedResponse = new HashMap<>(remoteData);
        combinedResponse.put("localId", personneOpt.get().getId());
        combinedResponse.put("localRole", personneOpt.get().getRole());

        return ResponseEntity.ok(combinedResponse);
    }


}
