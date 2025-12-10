package org.ust.project.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate appointmentDate;
    private String timeSlot;
    private String status;

    // Relationship: Many appointments can belong to One Patient
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    // Relationship: Many appointments can belong to One Doctor
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
}