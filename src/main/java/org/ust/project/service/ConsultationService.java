package org.ust.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ust.project.dto.AppointmentResponseDTO;
import org.ust.project.dto.BillResponseDTO;
import org.ust.project.dto.ConsultationRequestDTO;
import org.ust.project.dto.ConsultationResponseDTO;
import org.ust.project.dto.PrescriptionResponseDTO;
import org.ust.project.exception.ConsultationNotFoundException;
import org.ust.project.model.Appointment;
import org.ust.project.model.Bill;
import org.ust.project.model.Consultation;
import org.ust.project.model.Prescription;
import org.ust.project.repo.AppointmentRepository;
import org.ust.project.repo.ConsultationRepository;

@Service
public class ConsultationService {

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Create Consultation
    public ConsultationResponseDTO createConsultation(ConsultationRequestDTO consultationRequestDTO) {
        // Fetch the related appointment
        Appointment appointment = appointmentRepository.findById(consultationRequestDTO.getAppointmentId())
                .orElseThrow(() -> new ConsultationNotFoundException(consultationRequestDTO.getAppointmentId()));

        // Create and populate the consultation entity
        Consultation consultation = new Consultation();
        BeanUtils.copyProperties(consultationRequestDTO, consultation);

        // Set the appointment relationship
        consultation.setAppointment(appointment);

        // Save the consultation in the database
        consultation = consultationRepository.save(consultation);

        // Convert entity to DTO and return
        return convertToDTO(consultation);
    }

    // Update Consultation
    public ConsultationResponseDTO updateConsultation(Long id, ConsultationRequestDTO consultationRequestDTO) {
        Consultation existingConsultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ConsultationNotFoundException(id));

        // Update properties
        BeanUtils.copyProperties(consultationRequestDTO, existingConsultation, "id"); // Skip 'id' property while updating
        existingConsultation = consultationRepository.save(existingConsultation);

        return convertToDTO(existingConsultation);
    }

    // Get Consultation by ID
    public ConsultationResponseDTO getConsultationById(Long id) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ConsultationNotFoundException(id));
        return convertToDTO(consultation);
    }

    // Get All Consultations
    public List<ConsultationResponseDTO> getAllConsultations() {
        List<Consultation> consultations = consultationRepository.findAll();
        return consultations.stream()
                            .map(this::convertToDTO)
                            .collect(Collectors.toList());
    }

    // Delete Consultation
    public void deleteConsultation(Long id) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ConsultationNotFoundException(id));
        consultationRepository.delete(consultation);
    }

    // Convert entity to DTO
    private ConsultationResponseDTO convertToDTO(Consultation consultation) {
        ConsultationResponseDTO responseDTO = new ConsultationResponseDTO();
        BeanUtils.copyProperties(consultation, responseDTO);

        // Set the Appointment details in the response DTO
        Appointment appointment = consultation.getAppointment();
        if (appointment != null) {
            AppointmentResponseDTO appointmentResponseDTO = new AppointmentResponseDTO();
            BeanUtils.copyProperties(appointment, appointmentResponseDTO);
            responseDTO.setAppointment(appointmentResponseDTO);
        }

        // Set the Bill details in the response DTO
        Bill bill = consultation.getBill();
        if (bill != null) {
            BillResponseDTO billResponseDTO = new BillResponseDTO();
            BeanUtils.copyProperties(bill, billResponseDTO);
            responseDTO.setBill(billResponseDTO);
        }

        // Set the Prescription details in the response DTO
        Prescription prescription = consultation.getPrescription();
        if (prescription != null) {
            PrescriptionResponseDTO prescriptionResponseDTO = new PrescriptionResponseDTO();
            BeanUtils.copyProperties(prescription, prescriptionResponseDTO);
            responseDTO.setPrescription(prescriptionResponseDTO);
        }

        return responseDTO;
    }
}
