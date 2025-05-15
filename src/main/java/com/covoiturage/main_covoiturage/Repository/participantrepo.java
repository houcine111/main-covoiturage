package com.covoiturage.main_covoiturage.Repository;

import com.covoiturage.main_covoiturage.Entity.participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface participantrepo extends JpaRepository<participant, Long> {
    @Query("SELECT p FROM participant p WHERE p.trajet.conducteur.username = :username")
    List<participant> findByTrajetConducteurUsername(@Param("username") String username);
}
