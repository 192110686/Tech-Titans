package org.ust.project.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "bills")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate issueDate;
    private Double totalAmount;
    private String paymentStatus;
    private LocalDate dueDate;

    // Relationship: One bill can have one payment
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "payment_id")
    private Payment payment;
    
    // Relationship: One bill can belong to one patient
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "patient_id")
    private Patient patient;
    
    @OneToOne(mappedBy = "bill",cascade = CascadeType.ALL)
    @JsonManagedReference
    private Consultation consultation;
    
    public void calculateTotalFees(Double consultationFee, Double prescriptionFee) {
        this.totalAmount = consultationFee + prescriptionFee;
    }
}
