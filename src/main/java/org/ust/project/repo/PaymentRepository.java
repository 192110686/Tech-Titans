package org.ust.project.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ust.project.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Find payment by the bill ID it is associated with
    Payment findByBillId(Long billId);
}