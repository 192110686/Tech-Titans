package org.ust.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ust.project.dto.BillResponseDTO;
import org.ust.project.service.BillService;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;

    @Autowired
    public BillController(BillService billService) {
        this.billService = billService;
    }

    /* ================= CREATE BILL ================= */
    @PostMapping("/{consultationId}")
    public ResponseEntity<BillResponseDTO> createBill(@PathVariable Long consultationId) {
        BillResponseDTO billResponseDTO = billService.createBill(consultationId);
        return ResponseEntity.ok(billResponseDTO);
    }

    /* ================= GET BILL BY ID ================= */
    @GetMapping("/{id}")
    public ResponseEntity<BillResponseDTO> getBillById(@PathVariable Long id) {
        BillResponseDTO billResponseDTO = billService.getBillById(id);
        return ResponseEntity.ok(billResponseDTO);
    }

    /* ================= GET ALL BILLS ================= */
    @GetMapping
    public ResponseEntity<List<BillResponseDTO>> getAllBills() {
        List<BillResponseDTO> bills = billService.getAllBills();
        return ResponseEntity.ok(bills);
    }

   
}
