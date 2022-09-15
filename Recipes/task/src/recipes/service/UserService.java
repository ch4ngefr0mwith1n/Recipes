package recipes.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import recipes.model.User;
import recipes.repository.UserRepository;

import java.util.*;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Spring Security koristi ovu metodu prilikom verifikacije korisnika
     * potrebno je da vratimo korisničke podatke preko mejla i nakon toga prebacujemo te podatke u "UserDetails"
     * @param username
     * @return - "UserDetails" objekat
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optUser = userRepository.findByEmail(username);

        if (optUser.isEmpty()) {
            throw new UsernameNotFoundException("Not found:" + username);
        }

        return optUser.get();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void addUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // šifrovanje i ubacivanje lozinke:
        user.setPassword(getEncoder().encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
    }

    // PasswordEncoder koji koristimo za šifrovanje lozinke:
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

}
