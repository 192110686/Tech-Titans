package org.ust.project.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "inventory_items")
@Getter
@Setter
@ToString
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

    // Relationship: Many InventoryItems can belong to many Prescriptions
    @ManyToMany(mappedBy = "inventoryItems", fetch = FetchType.LAZY)
    @JsonManagedReference // To prevent infinite recursion during serialization
    private List<Prescription> prescriptions;
}
