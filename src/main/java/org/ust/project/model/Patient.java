package org.ust.project.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "patients")
@Getter
@Setter
@ToString(exclude = {"appointments","medicalRecords","bills"})
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
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // To prevent infinite recursion during serialization
    private List<Appointment> appointments;

    // Relationship: One patient can have many medical records
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // To prevent infinite recursion during serialization
    private List<MedicalRecord> medicalRecords;

    // Relationship: One patient can have many bills
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // To prevent infinite recursion during serialization
    private List<Bill> bills;

    // Relationship: One patient has one user account
    @OneToOne(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference // To avoid infinite recursion when serializing User
    private User user;
}
