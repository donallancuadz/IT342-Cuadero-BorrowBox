package edu.cit.cuadero.borrowbox.features.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import edu.cit.cuadero.borrowbox.features.user.DashboardResponse;
import edu.cit.cuadero.borrowbox.features.user.MeResponse;
import edu.cit.cuadero.borrowbox.features.user.UpdateProfileRequest;
import edu.cit.cuadero.borrowbox.shared.entity.User;
import edu.cit.cuadero.borrowbox.shared.repository.BorrowRequestRepository;
import edu.cit.cuadero.borrowbox.shared.repository.ItemRepository;
import edu.cit.cuadero.borrowbox.shared.repository.UserRepository;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;
    private final BorrowRequestRepository borrowRequestRepository;
    private final ItemRepository itemRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository,
                          BorrowRequestRepository borrowRequestRepository,
                          ItemRepository itemRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.borrowRequestRepository = borrowRequestRepository;
        this.itemRepository = itemRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getSubject();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(new MeResponse(
    user.getId(),
    user.getFullName(),
    user.getEmail(),
    user.getRole()));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getSubject();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        long activeBorrows   = borrowRequestRepository.countByUserAndStatus(user, "ACTIVE");
        long pendingRequests = borrowRequestRepository.countByUserAndStatus(user, "PENDING");
        long returnedItems   = borrowRequestRepository.countByUserAndStatus(user, "RETURNED");
        long availableItems  = itemRepository.countByAvailable(true); // uses YOUR existing method

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d");
        List<DashboardResponse.RecentRequestItem> recent =
            borrowRequestRepository.findTop5ByUserOrderByRequestDateDesc(user)
                .stream()
                .map(r -> new DashboardResponse.RecentRequestItem(
                        r.getItem().getName(),
                        r.getRequestDate().format(fmt),
                        r.getStatus()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new DashboardResponse(activeBorrows, pendingRequests, returnedItems, availableItems, recent));
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateMe(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody UpdateProfileRequest request) {

        String email = jwt.getSubject();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName().trim());
        }

        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            if (request.getCurrentPassword() == null || request.getCurrentPassword().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Current password is required to set a new password."));
            }
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Current password is incorrect."));
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        userRepository.save(user);
        return ResponseEntity.ok(new MeResponse(
    user.getId(),
    user.getFullName(),
    user.getEmail(),
    user.getRole()));
    }
}