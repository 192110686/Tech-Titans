package org.ust.project.repo;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ust.project.model.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    // Find patients by their phone number
    Optional<Patient> findByPhoneNumber(Long phoneNumber);
}