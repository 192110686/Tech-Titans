package org.ust.project.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ust.project.model.InventoryItem;

import java.util.List;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    // Find items running low on stock (e.g., less than 10 items)
    List<InventoryItem> findByQuantityLessThan(Double quantity);
    
    // Find items by category (e.g., "Surgical", "Medicine")
    List<InventoryItem> findByCategory(String category);
}