package com.covoiturage.main_covoiturage.Repository;

import com.covoiturage.main_covoiturage.Entity.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface Trajetrepo extends JpaRepository<Trajet, Long> {
    public Trajet findById(long id);
    @Query("SELECT t FROM Trajet t WHERE t.conducteur.id = :conducteurId AND t.depart = :depart AND t.heureDepart = :heure")

    Optional<Trajet> findBydepart(@Param("conducteurId") Long conducteurId,
                                         @Param("depart") String depart,
                                         @Param("heure") LocalTime heure);
    List<Trajet> findByParticipants_Id(Long personneId);
}
