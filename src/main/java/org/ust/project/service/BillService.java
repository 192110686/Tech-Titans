package org.ust.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ust.project.dto.AppointmentResponseDTO;
import org.ust.project.dto.BillRequestDTO;
import org.ust.project.dto.BillResponseDTO;
import org.ust.project.dto.DoctorResponseDTO;
import org.ust.project.dto.PatientResponseDTO;
import org.ust.project.exception.AppointmentNotFoundException;
import org.ust.project.exception.BillNotFoundException;
import org.ust.project.exception.PatientEntityNotFoundException;
import org.ust.project.model.Bill;
import org.ust.project.model.Appointment;
import org.ust.project.model.Patient;
import org.ust.project.repo.BillRepository;
import org.ust.project.repo.AppointmentRepository;
import org.ust.project.repo.PatientRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    // Create a new bill
    public BillResponseDTO createBill(BillRequestDTO billRequestDTO) {
        Bill bill = new Bill();

        // Fetch Appointment and Patient by ID
        Appointment appointment = appointmentRepository.findById(billRequestDTO.getAppointmentId())
            .orElseThrow(() -> new AppointmentNotFoundException(billRequestDTO.getAppointmentId()));

        Patient patient = patientRepository.findById(billRequestDTO.getPatientId())
            .orElseThrow(() -> new PatientEntityNotFoundException(billRequestDTO.getPatientId()));

        // Set the relationships and additional fields
        bill.setAppointment(appointment);
        bill.setPatient(patient);
        bill.setIssueDate(billRequestDTO.getIssueDate());
        bill.setTotalAmount(billRequestDTO.getTotalAmount());
        bill.setPaymentStatus(billRequestDTO.getPaymentStatus());
        bill.setDueDate(billRequestDTO.getDueDate());
        
        // Save the bill and ensure the appointment is updated
        appointment.setBill(bill);  // Link the bill to the appointment (set the back reference)
        
        // Save the bill and return the response DTO
        bill = billRepository.save(bill);
        
        Appointment ap = appointmentRepository.save(appointment);
        
        return new BillResponseDTO(
                bill.getId(),
                bill.getIssueDate(),
                bill.getTotalAmount(),
                bill.getPaymentStatus(),
                bill.getDueDate(),
                new AppointmentResponseDTO(  // Return the appointment details in the response
                    ap.getId(),
                    ap.getAppointmentDate(),
                    new DoctorResponseDTO(
                        ap.getDoctor().getId(),
                        ap.getDoctor().getFirstName(),
                        ap.getDoctor().getLastName(),
                        ap.getDoctor().getSpecialization(),
                        ap.getDoctor().getAvailabilitySchedule()
                    ),
                    new PatientResponseDTO(
                        appointment.getPatient().getId(),
                        appointment.getPatient().getFirstName(),
                        appointment.getPatient().getLastName(),
                        appointment.getPatient().getDateOfBirth(),
                        appointment.getPatient().getGender(),
                        appointment.getPatient().getPhoneNumber(),
                        appointment.getPatient().getEmail(),
                        appointment.getPatient().getBloodGroup()
                    )
                )
            );
        }

    // Get bill by ID
    public BillResponseDTO getBillById(Long id) {
        Optional<Bill> billOptional = billRepository.findById(id);
        if (billOptional.isPresent()) {
            Bill bill = billOptional.get();
            Appointment appointment = bill.getAppointment();

            // Create AppointmentResponseDTO for the bill's appointment
            AppointmentResponseDTO appointmentResponseDTO = new AppointmentResponseDTO(
                appointment.getId(),
                appointment.getAppointmentDate(),
                new DoctorResponseDTO(
                    appointment.getDoctor().getId(),
                    appointment.getDoctor().getFirstName(),
                    appointment.getDoctor().getLastName(),
                    appointment.getDoctor().getSpecialization(),
                    appointment.getDoctor().getAvailabilitySchedule()
                ),
                new PatientResponseDTO(
                    appointment.getPatient().getId(),
                    appointment.getPatient().getFirstName(),
                    appointment.getPatient().getLastName(),
                    appointment.getPatient().getDateOfBirth(),
                    appointment.getPatient().getGender(),
                    appointment.getPatient().getPhoneNumber(),
                    appointment.getPatient().getEmail(),
                    appointment.getPatient().getBloodGroup()
                )
            );

            // Return BillResponseDTO including AppointmentResponseDTO
            return new BillResponseDTO(
                bill.getId(),
                bill.getIssueDate(),
                bill.getTotalAmount(),
                bill.getPaymentStatus(),
                bill.getDueDate(),
                appointmentResponseDTO
            );
        }
        throw new BillNotFoundException(id);
    }

    // Get all bills
    public List<BillResponseDTO> getAllBills() {
        List<Bill> bills = billRepository.findAll();
        return bills.stream()
                .map(bill -> {
                    Appointment appointment = bill.getAppointment();
                    AppointmentResponseDTO appointmentResponseDTO = new AppointmentResponseDTO(
                        appointment.getId(),
                        appointment.getAppointmentDate(),
                        new DoctorResponseDTO(
                            appointment.getDoctor().getId(),
                            appointment.getDoctor().getFirstName(),
                            appointment.getDoctor().getLastName(),
                            appointment.getDoctor().getSpecialization(),
                            appointment.getDoctor().getAvailabilitySchedule()
                        ),
                        new PatientResponseDTO(
                            appointment.getPatient().getId(),
                            appointment.getPatient().getFirstName(),
                            appointment.getPatient().getLastName(),
                            appointment.getPatient().getDateOfBirth(),
                            appointment.getPatient().getGender(),
                            appointment.getPatient().getPhoneNumber(),
                            appointment.getPatient().getEmail(),
                            appointment.getPatient().getBloodGroup()
                        )
                    );

                    return new BillResponseDTO(
                        bill.getId(),
                        bill.getIssueDate(),
                        bill.getTotalAmount(),
                        bill.getPaymentStatus(),
                        bill.getDueDate(),
                        appointmentResponseDTO
                    );
                })
                .collect(Collectors.toList());
    }

    // Update bill
    public BillResponseDTO updateBill(Long id, BillRequestDTO billRequestDTO) {
        Optional<Bill> billOptional = billRepository.findById(id);
        if (billOptional.isPresent()) {
            Bill bill = billOptional.get();
            bill.setIssueDate(billRequestDTO.getIssueDate());
            bill.setTotalAmount(billRequestDTO.getTotalAmount());
            bill.setPaymentStatus(billRequestDTO.getPaymentStatus());
            bill.setDueDate(billRequestDTO.getDueDate());

            bill = billRepository.save(bill);

            Appointment appointment = bill.getAppointment();
            AppointmentResponseDTO appointmentResponseDTO = new AppointmentResponseDTO(
                appointment.getId(),
                appointment.getAppointmentDate(),
                new DoctorResponseDTO(
                    appointment.getDoctor().getId(),
                    appointment.getDoctor().getFirstName(),
                    appointment.getDoctor().getLastName(),
                    appointment.getDoctor().getSpecialization(),
                    appointment.getDoctor().getAvailabilitySchedule()
                ),
                new PatientResponseDTO(
                    appointment.getPatient().getId(),
                    appointment.getPatient().getFirstName(),
                    appointment.getPatient().getLastName(),
                    appointment.getPatient().getDateOfBirth(),
                    appointment.getPatient().getGender(),
                    appointment.getPatient().getPhoneNumber(),
                    appointment.getPatient().getEmail(),
                    appointment.getPatient().getBloodGroup()
                )
            );

            return new BillResponseDTO(
                bill.getId(),
                bill.getIssueDate(),
                bill.getTotalAmount(),
                bill.getPaymentStatus(),
                bill.getDueDate(),
                appointmentResponseDTO
            );
        }
        throw new BillNotFoundException(id);
    }

    // Delete bill
    public boolean deleteBill(Long id) {
        if (billRepository.existsById(id)) {
            billRepository.deleteById(id);
            return true;
        }
        throw new BillNotFoundException(id);
    }
}
