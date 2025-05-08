package com.covoiturage.main_covoiturage.Repository;

import com.covoiturage.main_covoiturage.Entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Adminrepo extends JpaRepository<Admin,Long> {
    Optional<Admin> findByUsername(String username);
}
