package org.ust.project.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "users") // 'user' is a reserved keyword in SQL, so 'users' is safer
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String role;
    private LocalDate createdAt;

    // Relationship: One user is linked to one patient
    @OneToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    // Relationship: One user is linked to one doctor
    @OneToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
}
