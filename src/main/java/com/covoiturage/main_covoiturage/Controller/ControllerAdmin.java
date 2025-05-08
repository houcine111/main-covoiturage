package com.covoiturage.main_covoiturage.Controller;

import com.covoiturage.main_covoiturage.Entity.Admin;
import com.covoiturage.main_covoiturage.Entity.Conducteur;
import com.covoiturage.main_covoiturage.Entity.Role;
import com.covoiturage.main_covoiturage.Repository.Adminrepo;
import com.covoiturage.main_covoiturage.Repository.Personnerepo;
import com.covoiturage.main_covoiturage.Services.Personneservice;
import com.covoiturage.main_covoiturage.security.jwt;
import com.covoiturage.main_covoiturage.security.jwtfilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@Data
@RestController
@RequestMapping("/admin")
public class ControllerAdmin {
    @Autowired
    private final Adminrepo adminrepo;
    @Autowired
    private  final Personneservice personneservice;
    @Autowired
    private final jwt jwt;

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @PostMapping("/login")
    public ResponseEntity<String>login(HttpServletRequest request , @RequestBody Admin admin) {
        Optional<Admin>ad=adminrepo.findByUsername(admin.getUsername());
        if(ad.isPresent()) {

            String token = jwt.generateToken(ad.get().getUsername());
            return ResponseEntity.ok(token);

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
    }
    @GetMapping("/dashboard")
    public ResponseEntity<?> afficherconducteur(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non connecté !");
        }

        String username = authentication.getName();
        System.out.println("Utilisateur authentifié: " + username);

        return ResponseEntity.ok(personneservice.afficherConducteur());
    }
}
