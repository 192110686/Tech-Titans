package org.ust.project.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ust.project.model.Appointment;
import org.ust.project.model.Consultation;

import java.util.List;
import java.time.LocalDate;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    
}