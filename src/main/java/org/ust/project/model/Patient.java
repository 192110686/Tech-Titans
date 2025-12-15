package org.ust.project.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private Long phoneNumber;
    private String address;
    private String email;
    private LocalDate registrationDate;
    private String bloodGroup;

    // Relationship: One patient can have many appointments
    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments;

    // Relationship: One patient can have many medical records
    @OneToMany(mappedBy = "patient")
    private List<MedicalRecord> medicalRecords;

    // Relationship: One patient can have many bills
    @OneToMany(mappedBy = "patient")
    private List<Bill> bills;

    // Relationship: One patient has one user account
    @OneToOne(mappedBy = "patient")
    private User user;
}
