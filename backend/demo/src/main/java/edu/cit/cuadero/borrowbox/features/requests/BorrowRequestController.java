package edu.cit.cuadero.borrowbox.features.requests;

import edu.cit.cuadero.borrowbox.features.requests.BorrowRequestCreateRequest;
import edu.cit.cuadero.borrowbox.features.requests.BorrowRequestResponse;
import edu.cit.cuadero.borrowbox.features.requests.BorrowRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class BorrowRequestController {

    private final BorrowRequestService borrowRequestService;

    public BorrowRequestController(BorrowRequestService borrowRequestService) {
        this.borrowRequestService = borrowRequestService;
    }

    @PostMapping
    public ResponseEntity<BorrowRequestResponse> createRequest(
            @RequestBody BorrowRequestCreateRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(borrowRequestService.createRequest(request, authentication));
    }

    @GetMapping("/my")
    public ResponseEntity<List<BorrowRequestResponse>> getMyRequests(Authentication authentication) {
        return ResponseEntity.ok(borrowRequestService.getMyRequests(authentication));
    }
}