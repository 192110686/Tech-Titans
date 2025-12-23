package org.ust.project.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "prescriptions")
@Getter
@Setter
@ToString(exclude = "inventoryItems")
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
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "appointment_id")
    // @JsonBackReference // To avoid infinite recursion during serialization
    // private Appointment appointment;

//    // Relationship: Many prescriptions belong to one medical record
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "medical_record_id")
//    @JsonBackReference // To avoid infinite recursion during serialization
//    private MedicalRecord medicalRecord;
    
    @OneToOne(mappedBy = "prescription" , cascade = CascadeType.ALL)
    @JsonManagedReference
    private Consultation consultation;

    // Relationship: Many prescriptions can involve many inventory items
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "prescription_inventory",
        joinColumns = @JoinColumn(name = "prescription_id"),
        inverseJoinColumns = @JoinColumn(name = "inventory_item_id")
    )
    @JsonManagedReference // To avoid infinite recursion during serialization
    private List<InventoryItem> inventoryItems;
}
