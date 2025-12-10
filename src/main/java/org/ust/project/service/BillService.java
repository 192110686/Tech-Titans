package org.ust.project.service;

import org.ust.project.model.Bill;
import org.ust.project.repo.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public Optional<Bill> getBillById(Long id) {
        return billRepository.findById(id);
    }

    public Bill saveBill(Bill bill) {
        return billRepository.save(bill);
    }

    public Bill updateBill(Long id, Bill billDetails) {
        Optional<Bill> optionalBill = billRepository.findById(id);
        
        if (optionalBill.isPresent()) {
            Bill existingBill = optionalBill.get();
            existingBill.setTotalAmount(billDetails.getTotalAmount());
            existingBill.setPaymentStatus(billDetails.getPaymentStatus());
            existingBill.setDueDate(billDetails.getDueDate());
            existingBill.setIssueDate(billDetails.getIssueDate());
            return billRepository.save(existingBill);
        }
        return null;
    }

    public void deleteBill(Long id) {
        billRepository.deleteById(id);
    }
}