package edu.cit.cuadero.borrowbox.repository;

import edu.cit.cuadero.borrowbox.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    long countByAvailable(Boolean available);
}
