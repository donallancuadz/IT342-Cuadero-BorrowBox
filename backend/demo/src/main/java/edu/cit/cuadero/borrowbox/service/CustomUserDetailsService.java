package edu.cit.cuadero.borrowbox.service;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import edu.cit.cuadero.borrowbox.entity.User;
import edu.cit.cuadero.borrowbox.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword()) // BCrypt hash from DB
                .roles("USER")
                .build();
    }
}