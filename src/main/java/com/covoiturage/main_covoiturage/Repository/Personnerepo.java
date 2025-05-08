package com.covoiturage.main_covoiturage.Repository;

import com.covoiturage.main_covoiturage.Entity.Personne;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Personnerepo extends JpaRepository<Personne, Long> {
    Optional<Personne> findByUsername(String username);
}
