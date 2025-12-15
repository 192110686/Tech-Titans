package org.ust.project.model;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventory_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;
    private String category;
    private Double unitPrice;
    private Double quantity;
    private String supplier;
    private String description;
    
    @ManyToMany(mappedBy = "inventoryItems")
    private List<Prescription> prescriptions;
}
