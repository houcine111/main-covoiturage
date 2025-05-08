package com.covoiturage.main_covoiturage.Services;

import com.covoiturage.main_covoiturage.Entity.Admin;
import com.covoiturage.main_covoiturage.Repository.Adminrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class Adminservice implements UserDetailsService {
    @Autowired
    private Adminrepo adminrepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Admin> admin= adminrepo.findByUsername(username);
        if (admin.isPresent()) {
            var ad = admin.get();
            return User.builder()
                    .username(ad.getUsername())
                    .password(ad.getPassword())

                    .build();
        } else throw new UsernameNotFoundException(username);

    }

}
