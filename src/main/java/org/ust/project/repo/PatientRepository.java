package org.ust.project.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ust.project.model.Patient;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    // Find patients by their phone number
    Optional<Patient> findByPhoneNumber(Long phoneNumber);
}