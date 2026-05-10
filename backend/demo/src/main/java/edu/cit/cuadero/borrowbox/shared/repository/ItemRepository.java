package edu.cit.cuadero.borrowbox.shared.repository;

import edu.cit.cuadero.borrowbox.shared.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    long countByAvailable(Boolean available);
}
