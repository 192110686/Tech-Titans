package org.ust.project.model;

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
@Table(name = "doctors")
@Getter
@Setter
@ToString(exclude = {"appointments", "consultancyRecords", "prescriptions"})
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String specialization;  // Doctor's specialization
    private String qualifications;  // Qualifications, such as MBBS, MD, etc.
    private Long contactNumber;
    private String email;
    private String licenseNumber;
    private String availabilitySchedule; // Could be a JSON string for flexible scheduling

    // Relationship: One doctor can have many appointments
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // To prevent infinite recursion during serialization
    private List<Appointment> appointments;

    // // Relationship: One doctor can have many consultancy records (this would be linked to the patient's consultation)
    // @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    // @JsonManagedReference // To avoid infinite recursion when serializing ConsultancyRecord
    // private List<ConsultancyRecord> consultancyRecords;

    // Relationship: One doctor can write many prescriptions
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // To avoid infinite recursion during serialization
    private List<Prescription> prescriptions;

    // Relationship: One doctor has one user account
    @OneToOne(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference // To avoid infinite recursion when serializing User
    private User user;

}
