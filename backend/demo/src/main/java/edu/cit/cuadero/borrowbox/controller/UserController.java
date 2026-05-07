package edu.cit.cuadero.borrowbox.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import edu.cit.cuadero.borrowbox.dto.MeResponse;
import edu.cit.cuadero.borrowbox.dto.UpdateProfileRequest;
import edu.cit.cuadero.borrowbox.entity.User;
import edu.cit.cuadero.borrowbox.repository.UserRepository;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getSubject();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(new MeResponse(user.getId(), user.getFullName(), user.getEmail(), user.getRole()));
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateMe(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody UpdateProfileRequest req
    ) {
        String email = jwt.getSubject();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (req.getFullName() != null && !req.getFullName().trim().isEmpty()) {
            user.setFullName(req.getFullName().trim());
        }

        if (req.getNewPassword() != null && !req.getNewPassword().isBlank()) {
            if (req.getCurrentPassword() == null || req.getCurrentPassword().isBlank()) {
                return ResponseEntity.badRequest().body("Current password is required");
            }

            if (!passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())) {
                return ResponseEntity.badRequest().body("Current password is incorrect");
            }

            user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        }

        User updated = userRepository.save(user);
        return ResponseEntity.ok(new MeResponse(updated.getId(), updated.getFullName(), updated.getEmail(), updated.getRole()));
    }
}
