package org.ust.project.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate paymentDate;
    private Double amountPaid;
    private String paymentMethod;

    @OneToOne // Assuming one payment pays off one bill. Could be @ManyToOne if partial payments exist.
    @JoinColumn(name = "bill_id")
    private Bill bill;
}