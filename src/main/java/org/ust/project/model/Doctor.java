package org.ust.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // To prevent infinite recursion during serialization
    private List<Appointment> appointments;

    // Relationship: One doctor can have many medical records
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // To prevent infinite recursion during serialization
    private List<MedicalRecord> medicalRecords;

    // Relationship: One doctor has one user account
    @OneToOne(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference // To avoid infinite recursion when serializing User
    private User user;
}
