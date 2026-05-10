package edu.cit.cuadero.borrowbox.features.admin;

import edu.cit.cuadero.borrowbox.features.requests.AdminBorrowRequestResponse;
import edu.cit.cuadero.borrowbox.features.admin.AdminStatsResponse;
import edu.cit.cuadero.borrowbox.features.items.ItemRequest;
import edu.cit.cuadero.borrowbox.features.admin.RoleUpdateRequest;
import edu.cit.cuadero.borrowbox.features.admin.UserAdminResponse;
import edu.cit.cuadero.borrowbox.shared.entity.BorrowRequest;
import edu.cit.cuadero.borrowbox.shared.entity.Item;
import edu.cit.cuadero.borrowbox.shared.entity.User;
import edu.cit.cuadero.borrowbox.shared.repository.BorrowRequestRepository;
import edu.cit.cuadero.borrowbox.shared.repository.ItemRepository;
import edu.cit.cuadero.borrowbox.shared.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ItemRepository itemRepository;
    private final BorrowRequestRepository borrowRequestRepository;
    private final UserRepository userRepository;

    public AdminController(
            ItemRepository itemRepository,
            BorrowRequestRepository borrowRequestRepository,
            UserRepository userRepository
    ) {
        this.itemRepository = itemRepository;
        this.borrowRequestRepository = borrowRequestRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<?> stats(@AuthenticationPrincipal Jwt jwt) {
        ResponseEntity<?> forbidden = requireAdmin(jwt);
        if (forbidden != null) return forbidden;

        return ResponseEntity.ok(new AdminStatsResponse(
                userRepository.count(),
                userRepository.countByRole("ADMIN"),
                itemRepository.count(),
                itemRepository.countByAvailable(true),
                borrowRequestRepository.count(),
                borrowRequestRepository.countByStatus("PENDING"),
                borrowRequestRepository.countByStatus("APPROVED"),
                borrowRequestRepository.countByStatus("REJECTED")
        ));
    }

    @GetMapping("/items")
    public ResponseEntity<?> items(@AuthenticationPrincipal Jwt jwt) {
        ResponseEntity<?> forbidden = requireAdmin(jwt);
        if (forbidden != null) return forbidden;
        return ResponseEntity.ok(itemRepository.findAll());
    }

    @PostMapping("/items")
    public ResponseEntity<?> createItem(@AuthenticationPrincipal Jwt jwt, @RequestBody ItemRequest req) {
        ResponseEntity<?> forbidden = requireAdmin(jwt);
        if (forbidden != null) return forbidden;

        Item item = new Item();
        applyItemRequest(item, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemRepository.save(item));
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<?> updateItem(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id,
            @RequestBody ItemRequest req
    ) {
        ResponseEntity<?> forbidden = requireAdmin(jwt);
        if (forbidden != null) return forbidden;

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));
        applyItemRequest(item, req);
        return ResponseEntity.ok(itemRepository.save(item));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<?> deleteItem(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        ResponseEntity<?> forbidden = requireAdmin(jwt);
        if (forbidden != null) return forbidden;

        if (!itemRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
        }
        itemRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/requests")
    public ResponseEntity<?> requests(@AuthenticationPrincipal Jwt jwt) {
        ResponseEntity<?> forbidden = requireAdmin(jwt);
        if (forbidden != null) return forbidden;

        List<AdminBorrowRequestResponse> requests = borrowRequestRepository.findAll()
                .stream()
                .map(this::toAdminBorrowRequestResponse)
                .toList();
        return ResponseEntity.ok(requests);
    }

    @PutMapping("/requests/{id}/approve")
    public ResponseEntity<?> approveRequest(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        return decideRequest(jwt, id, "APPROVED");
    }

    @PutMapping("/requests/{id}/reject")
    public ResponseEntity<?> rejectRequest(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        return decideRequest(jwt, id, "REJECTED");
    }

    @GetMapping("/users")
    public ResponseEntity<?> users(@AuthenticationPrincipal Jwt jwt) {
        ResponseEntity<?> forbidden = requireAdmin(jwt);
        if (forbidden != null) return forbidden;

        List<UserAdminResponse> users = userRepository.findAll()
                .stream()
                .map(user -> new UserAdminResponse(
                        user.getId(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getStudentId(),
                        user.getRole()
                ))
                .toList();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> updateRole(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id,
            @RequestBody RoleUpdateRequest req
    ) {
        ResponseEntity<?> forbidden = requireAdmin(jwt);
        if (forbidden != null) return forbidden;

        String role = req.getRole() == null ? "" : req.getRole().trim().toUpperCase();
        if (!role.equals("ADMIN") && !role.equals("USER")) {
            return ResponseEntity.badRequest().body("Role must be ADMIN or USER");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setRole(role);
        User saved = userRepository.save(user);

        return ResponseEntity.ok(new UserAdminResponse(
                saved.getId(),
                saved.getFullName(),
                saved.getEmail(),
                saved.getStudentId(),
                saved.getRole()
        ));
    }

    private ResponseEntity<?> decideRequest(Jwt jwt, Long id, String status) {
        ResponseEntity<?> forbidden = requireAdmin(jwt);
        if (forbidden != null) return forbidden;

        BorrowRequest request = borrowRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        request.setStatus(status);
        request.getItem().setAvailable(!status.equals("APPROVED"));
        BorrowRequest saved = borrowRequestRepository.save(request);
        itemRepository.save(saved.getItem());

        return ResponseEntity.ok(toAdminBorrowRequestResponse(saved));
    }

    private void applyItemRequest(Item item, ItemRequest req) {
        item.setName(required(req.getName(), "Name"));
        item.setCategory(required(req.getCategory(), "Category"));
        item.setDescription(required(req.getDescription(), "Description"));
        item.setAvailable(req.getAvailable() == null || req.getAvailable());
        item.setImageUrl(req.getImageUrl());
    }

    private String required(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " is required");
        }
        return value.trim();
    }

    private AdminBorrowRequestResponse toAdminBorrowRequestResponse(BorrowRequest request) {
        return new AdminBorrowRequestResponse(
                request.getId(),
                request.getItem().getId(),
                request.getItem().getName(),
                request.getUser().getId(),
                request.getUser().getFullName(),
                request.getUser().getEmail(),
                request.getStatus(),
                request.getRequestDate()
        );
    }

    private ResponseEntity<?> requireAdmin(Jwt jwt) {
        String role = jwt.getClaimAsString("role");
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required");
        }
        return null;
    }
}
