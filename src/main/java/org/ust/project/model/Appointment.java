package org.ust.project.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Entity
@Table(name = "appointments")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime appointmentDateTime; // Changed to LocalDateTime to hold both date and time
    private String status;
    private String reasonForVisit;
  

    // Relationship: Many appointments belong to one patient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    @JsonBackReference // To avoid infinite recursion when serializing Patient
    private Patient patient;

    // Relationship: Many appointments belong to one doctor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    @JsonBackReference // To avoid infinite recursion when serializing Doctor
    private Doctor doctor;
    
    @OneToOne(mappedBy = "appointment",cascade = CascadeType.ALL)
    @JsonManagedReference
    private Consultation consultation;

    // Getter for appointmentDateTime
    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    // Setter for appointmentDateTime
    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }
}
