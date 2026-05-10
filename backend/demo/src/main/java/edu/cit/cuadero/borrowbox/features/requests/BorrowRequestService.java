package edu.cit.cuadero.borrowbox.features.requests;

import edu.cit.cuadero.borrowbox.features.requests.BorrowRequestCreateRequest;
import edu.cit.cuadero.borrowbox.features.requests.BorrowRequestResponse;
import edu.cit.cuadero.borrowbox.shared.entity.BorrowRequest;
import edu.cit.cuadero.borrowbox.shared.entity.Item;
import edu.cit.cuadero.borrowbox.shared.entity.User;
import edu.cit.cuadero.borrowbox.shared.repository.BorrowRequestRepository;
import edu.cit.cuadero.borrowbox.shared.repository.ItemRepository;
import edu.cit.cuadero.borrowbox.shared.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowRequestService {

    private final BorrowRequestRepository borrowRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public BorrowRequestService(
            BorrowRequestRepository borrowRequestRepository,
            ItemRepository itemRepository,
            UserRepository userRepository
    ) {
        this.borrowRequestRepository = borrowRequestRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public BorrowRequestResponse createRequest(BorrowRequestCreateRequest req, Authentication authentication) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Item item = itemRepository.findById(req.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        if (!Boolean.TRUE.equals(item.getAvailable())) {
            throw new IllegalArgumentException("Item is unavailable");
        }

        BorrowRequest borrowRequest = new BorrowRequest();
        borrowRequest.setUser(user);
        borrowRequest.setItem(item);
        borrowRequest.setStatus("PENDING");

        BorrowRequest saved = borrowRequestRepository.save(borrowRequest);

        return new BorrowRequestResponse(
                saved.getId(),
                saved.getItem().getId(),
                saved.getItem().getName(),
                saved.getStatus(),
                saved.getRequestDate()
        );
    }

    public List<BorrowRequestResponse> getMyRequests(Authentication authentication) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return borrowRequestRepository.findByUser(user)
                .stream()
                .map(br -> new BorrowRequestResponse(
                        br.getId(),
                        br.getItem().getId(),
                        br.getItem().getName(),
                        br.getStatus(),
                        br.getRequestDate()
                ))
                .toList();
    }
}