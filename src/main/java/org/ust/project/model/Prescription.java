package org.ust.project.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "prescriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medicationName;
    private Double dosageMg;
    private Double price;
    private Double frequency;
    private LocalDate startDate;
    private LocalDate endDate;

    // Relationship: Many prescriptions belong to one appointment
    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    // Relationship: Many prescriptions belong to one medical record
    @ManyToOne
    @JoinColumn(name = "medical_record_id")
    private MedicalRecord medicalRecord;

    // Relationship: Many prescriptions can involve many inventory items
    @ManyToMany
    @JoinTable(
        name = "prescription_inventory",
        joinColumns = @JoinColumn(name = "prescription_id"),
        inverseJoinColumns = @JoinColumn(name = "inventory_item_id")
    )
    private List<InventoryItem> inventoryItems;
}
