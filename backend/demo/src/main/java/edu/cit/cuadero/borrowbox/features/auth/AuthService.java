package edu.cit.cuadero.borrowbox.features.auth;


import java.time.Instant;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import edu.cit.cuadero.borrowbox.features.auth.RegisterRequest;
import edu.cit.cuadero.borrowbox.shared.entity.User;
import edu.cit.cuadero.borrowbox.shared.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    @Value("${app.jwt.issuer}")
    private String issuer;

    @Value("${app.jwt.exp-minutes}")
    private long expMinutes;

    @Value("${app.admin.email:}")
    private String adminEmail;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtEncoder jwtEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    // =========================
    // REGISTER
    // =========================
    public User register(RegisterRequest req) {

        String email = req.getEmail().trim().toLowerCase();

        // 1. Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already registered");
        }

        // 2. Check password match
        if (!req.getPassword().equals(req.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // 3. Validate Student ID format (XX-XXXX-XXX)
        if (!req.getStudentId().matches("^[0-9]{2}-[0-9]{4}-[0-9]{3}$")) {
            throw new IllegalArgumentException("Invalid Student ID format (XX-XXXX-XXX)");
        }

        // 4. Hash password
        String hashedPassword = passwordEncoder.encode(req.getPassword());

        // 5. Create user
        User user = new User();
        user.setFullName(req.getFullName().trim());
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setStudentId(req.getStudentId());

        return userRepository.save(user);
    }

    // =========================
    // LOGIN (verify credentials)
    // =========================
    public User login(String email, String rawPassword) {
        String cleanEmail = email.trim().toLowerCase();

        User user = userRepository.findByEmail(cleanEmail)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        boolean isConfiguredAdmin = !adminEmail.isBlank() && cleanEmail.equals(adminEmail.trim().toLowerCase());
        boolean hasNoAdmins = userRepository.countByRole("ADMIN") == 0;

        if ((isConfiguredAdmin || hasNoAdmins) && !"ADMIN".equals(user.getRole())) {
            user.setRole("ADMIN");
            user = userRepository.save(user);
        }

        return user;
    }

    // =========================
    // JWT TOKEN GENERATION
    // =========================
    public String generateToken(User user) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(expMinutes * 60);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(expiry)
                .subject(user.getEmail())      // subject = email
                .claim("uid", user.getId())
                .claim("name", user.getFullName())
                .claim("role", user.getRole())
                .build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(
                        JwsHeader.with(MacAlgorithm.HS256).build(),
                        claims
                )
        ).getTokenValue();
    }
}
