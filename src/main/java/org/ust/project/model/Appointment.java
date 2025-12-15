package org.ust.project.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

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
    private String reasonForVisit;
    private Long doctorId;
    private Long patientId;

    // Relationship: Many appointments belong to one patient
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    // Relationship: Many appointments belong to one doctor
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    // Relationship: One appointment can have one bill
    @OneToOne(mappedBy = "appointment")
    private Bill bill;

    // Relationship: One appointment can have many prescriptions
    @OneToMany(mappedBy = "appointment")
    private List<Prescription> prescriptions;
}
