package com.covoiturage.main_covoiturage.Repository;

import com.covoiturage.main_covoiturage.Entity.Conducteur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface Conducteurrepo extends JpaRepository<Conducteur, Integer> {
    Optional<Conducteur> findByUsername(String username);


}
