package edu.cit.cuadero.borrowbox.repository;

import edu.cit.cuadero.borrowbox.entity.BorrowRequest;
import edu.cit.cuadero.borrowbox.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRequestRepository extends JpaRepository<BorrowRequest, Long> {
    List<BorrowRequest> findByUser(User user);
    long countByStatus(String status);
}
