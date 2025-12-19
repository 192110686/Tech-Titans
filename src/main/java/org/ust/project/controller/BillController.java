package org.ust.project.controller;  // Corrected package name

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ust.project.dto.BillRequestDTO;
import org.ust.project.dto.BillResponseDTO;
import org.ust.project.service.BillService;

@RestController
@RequestMapping("/bills")  // Base URL for all Bill-related endpoints
public class BillController {

    @Autowired
    private BillService billService;

    // Endpoint to create a new bill
    @PostMapping
    public ResponseEntity<BillResponseDTO> createBill(@RequestBody BillRequestDTO billRequestDTO) {
        BillResponseDTO billResponseDTO = billService.createBill(billRequestDTO);
        if (billResponseDTO != null) {
            return ResponseEntity.ok(billResponseDTO);
        }
        return ResponseEntity.badRequest().build();  // Return 400 Bad Request if the bill creation fails
    }

    // Endpoint to get a bill by ID
    @GetMapping("/{id}")
    public ResponseEntity<BillResponseDTO> getBillById(@PathVariable Long id) {
        BillResponseDTO billResponseDTO = billService.getBillById(id);
        if (billResponseDTO != null) {
            return ResponseEntity.ok(billResponseDTO);
        }
        return ResponseEntity.notFound().build();  // Return 404 Not Found if the bill is not found
    }

    // Endpoint to get all bills
    @GetMapping
    public ResponseEntity<List<BillResponseDTO>> getAllBills() {
        List<BillResponseDTO> bills = billService.getAllBills();
        return ResponseEntity.ok(bills);  // Return a list of all bills
    }

    // Endpoint to update a bill
    @PutMapping("/{id}")
    public ResponseEntity<BillResponseDTO> updateBill(@PathVariable Long id, @RequestBody BillRequestDTO billRequestDTO) {
        BillResponseDTO billResponseDTO = billService.updateBill(id, billRequestDTO);
        if (billResponseDTO != null) {
            return ResponseEntity.ok(billResponseDTO);
        }
        return ResponseEntity.notFound().build();  // Return 404 Not Found if the bill to update is not found
    }

    // Endpoint to delete a bill
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        boolean deleted = billService.deleteBill(id);
        if (deleted) {
            return ResponseEntity.noContent().build();  // Return 204 No Content if the bill is deleted
        }
        return ResponseEntity.notFound().build();  // Return 404 Not Found if the bill is not found
    }
}
