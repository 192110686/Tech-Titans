package org.ust.project.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PrescriptionRequestDTO {

    private String medicationName;

    private Double dosageMg;

    private Double price;

    private Double frequency;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long appointmentId;  // Linking to the Appointment

    private Long medicalRecordId;  // Linking to the MedicalRecord


    public PrescriptionRequestDTO(Long appointmentId, Double dosageMg, LocalDate endDate, Double frequency, Long medicalRecordId, String medicationName, Double price, LocalDate startDate) {
        this.appointmentId = appointmentId;
        this.dosageMg = dosageMg;
        this.endDate = endDate;
        this.frequency = frequency;
        this.medicalRecordId = medicalRecordId;
        this.medicationName = medicationName;
        this.price = price;
        this.startDate = startDate;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public Double getDosageMg() {
        return dosageMg;
    }

    public void setDosageMg(Double dosageMg) {
        this.dosageMg = dosageMg;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getFrequency() {
        return frequency;
    }

    public void setFrequency(Double frequency) {
        this.frequency = frequency;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(Long medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PrescriptionRequestDTO{");
        sb.append("medicationName=").append(medicationName);
        sb.append(", dosageMg=").append(dosageMg);
        sb.append(", price=").append(price);
        sb.append(", frequency=").append(frequency);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", appointmentId=").append(appointmentId);
        sb.append(", medicalRecordId=").append(medicalRecordId);
        sb.append('}');
        return sb.toString();
    }
    

}
