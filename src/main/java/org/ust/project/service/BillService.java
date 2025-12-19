package org.ust.project.service;

import org.ust.project.dto.BillRequestDTO;
import org.ust.project.dto.BillResponseDTO;
import org.ust.project.model.Bill;
import org.ust.project.repo.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    // Create a new bill
    public BillResponseDTO createBill(BillRequestDTO billRequestDTO) {
        Bill bill = new Bill();
        bill.setIssueDate(billRequestDTO.getIssueDate());
        bill.setTotalAmount(billRequestDTO.getTotalAmount());
        bill.setPaymentStatus(billRequestDTO.getPaymentStatus());
        bill.setDueDate(billRequestDTO.getDueDate());

        bill = billRepository.save(bill);
        return new BillResponseDTO(bill.getId(), bill.getIssueDate(), bill.getTotalAmount(), bill.getPaymentStatus(), bill.getDueDate());
    }

    // Get bill by ID
    public BillResponseDTO getBillById(Long id) {
        Optional<Bill> billOptional = billRepository.findById(id);
        if (billOptional.isPresent()) {
            Bill bill = billOptional.get();
            return new BillResponseDTO(bill.getId(), bill.getIssueDate(), bill.getTotalAmount(), bill.getPaymentStatus(), bill.getDueDate());
        }
        return null;
    }

    // Get all bills
    public List<BillResponseDTO> getAllBills() {
        List<Bill> bills = billRepository.findAll();
        return bills.stream()
                .map(bill -> new BillResponseDTO(bill.getId(), bill.getIssueDate(), bill.getTotalAmount(), bill.getPaymentStatus(), bill.getDueDate()))
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
            return new BillResponseDTO(bill.getId(), bill.getIssueDate(), bill.getTotalAmount(), bill.getPaymentStatus(), bill.getDueDate());
        }
        return null;
    }

    // Delete bill
    public boolean deleteBill(Long id) {
        if (billRepository.existsById(id)) {
            billRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
