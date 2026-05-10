package edu.cit.cuadero.borrowbox.features.auth;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.cit.cuadero.borrowbox.features.auth.LoginRequest;
import edu.cit.cuadero.borrowbox.features.auth.LoginResponse;
import edu.cit.cuadero.borrowbox.features.auth.RegisterRequest;
import edu.cit.cuadero.borrowbox.features.auth.RegisterResponse;
import edu.cit.cuadero.borrowbox.shared.entity.User;
import edu.cit.cuadero.borrowbox.features.auth.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // =========================
    // REGISTER
    // =========================
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        try {
            User user = authService.register(req);
            RegisterResponse res = new RegisterResponse(
                    user.getId(),
                    user.getFullName(),
                    user.getEmail()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // =========================
    // LOGIN (returns JWT token)
    // =========================
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        try {
            User user = authService.login(req.getEmail(), req.getPassword());
            String token = authService.generateToken(user);

            LoginResponse res = new LoginResponse(
                    token,
                    user.getId(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getRole()
            );

            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
    }

    // =========================
    // LOGOUT (JWT is stateless)
    // =========================
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // JWT is stateless: logout is handled on the client by deleting the stored token
        return ResponseEntity.ok("Logged out (client token cleared).");
    }
}
