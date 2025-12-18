package org.ust.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "bills")
@Data
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

    // Relationship: One bill can be associated with one appointment
    @OneToOne(mappedBy = "bill", fetch = FetchType.LAZY)
    private Appointment appointment;

    // Relationship: One bill can have one payment
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "payment_id")
    private Payment payment;
    
    // Relationship: One bill can belong to one patient
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "patient_id")
    private Patient patient;
}
