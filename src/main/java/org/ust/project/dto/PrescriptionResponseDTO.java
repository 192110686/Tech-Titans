package org.ust.project.dto;
import java.time.LocalDate;

public class PrescriptionResponseDTO {

    private Long id;

    private String medicationName;

    private Double dosageMg;

    private Double price;

    private Double frequency;

    private LocalDate startDate;

    private LocalDate endDate;

    private AppointmentResponseDTO appointment;  // Nested DTO for appointment details

    private MedicalRecordResponseDTO medicalRecord; 
    
    public PrescriptionResponseDTO(){}

    public PrescriptionResponseDTO(AppointmentResponseDTO appointment, Double dosageMg, LocalDate endDate, Double frequency, Long id, MedicalRecordResponseDTO medicalRecord, String medicationName, Double price, LocalDate startDate) {
        this.appointment = appointment;
        this.dosageMg = dosageMg;
        this.endDate = endDate;
        this.frequency = frequency;
        this.id = id;
        this.medicalRecord = medicalRecord;
        this.medicationName = medicationName;
        this.price = price;
        this.startDate = startDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public AppointmentResponseDTO getAppointment() {
        return appointment;
    }

    public void setAppointment(AppointmentResponseDTO appointment) {
        this.appointment = appointment;
    }

    public MedicalRecordResponseDTO getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecordResponseDTO medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PrescriptionResponseDTO{");
        sb.append("id=").append(id);
        sb.append(", medicationName=").append(medicationName);
        sb.append(", dosageMg=").append(dosageMg);
        sb.append(", price=").append(price);
        sb.append(", frequency=").append(frequency);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", appointment=").append(appointment);
        sb.append(", medicalRecord=").append(medicalRecord);
        sb.append('}');
        return sb.toString();
    }
    

}
