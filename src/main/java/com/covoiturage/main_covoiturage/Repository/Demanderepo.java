package com.covoiturage.main_covoiturage.Repository;

import com.covoiturage.main_covoiturage.Entity.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface Demanderepo extends JpaRepository<Demande, Long> {
    List<Demande> findByTrajetConducteurUsername(String  username);

}
