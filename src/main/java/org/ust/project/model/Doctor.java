package org.ust.project.model;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String specialization;
    private Long contactNumber;
    private String email;
    private String licenseNumber;
    private String availabilitySchedule;

    // Relationship: One doctor can have many appointments
    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments;

    // Relationship: One doctor can have many medical records
    @OneToMany(mappedBy = "doctor")
    private List<MedicalRecord> medicalRecords;

    // Relationship: One doctor has one user account
    @OneToOne
    private User user;
}
