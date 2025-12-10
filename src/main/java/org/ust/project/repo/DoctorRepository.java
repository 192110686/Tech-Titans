package org.ust.project.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ust.project.model.Doctor;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    // Find doctors by their specialization (e.g., "Cardiologist")
    List<Doctor> findBySpecialization(String specialization);
}