package org.ust.project.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

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
    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    // Relationship: One bill can have many payments
    @OneToMany(mappedBy = "bill")
    private List<Payment> payments;
}
