package org.ust.project.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.ust.project.dto.BillRequestDTO;
import org.ust.project.dto.BillResponseDTO;
import org.ust.project.service.BillService;

@RestController
@RequestMapping("/bills")
public class BillController {

    @Autowired
    private BillService billService;

    /* ================= CREATE ================= */
    @PostMapping
    public ResponseEntity<BillResponseDTO> createBill(
            @Valid @RequestBody BillRequestDTO requestDTO) {

        BillResponseDTO response =
                billService.createBill(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /* ================= GET BY ID ================= */
    @GetMapping("/{id}")
    public ResponseEntity<BillResponseDTO> getBillById(@PathVariable Long id) {

        BillResponseDTO response =
                billService.getBillById(id);

        return ResponseEntity.ok(response);
    }

    /* ================= GET ALL ================= */
    @GetMapping
    public ResponseEntity<List<BillResponseDTO>> getAllBills() {

        List<BillResponseDTO> responses =
                billService.getAllBills();

        return ResponseEntity.ok(responses);
    }

    /* ================= UPDATE ================= */
    @PutMapping("/{id}")
    public ResponseEntity<BillResponseDTO> updateBill(
            @PathVariable Long id,
            @Valid @RequestBody BillRequestDTO requestDTO) {

        BillResponseDTO response =
                billService.updateBill(id, requestDTO);

        return ResponseEntity.ok(response);
    }

    /* ================= DELETE ================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {

        billService.deleteBill(id);

        return ResponseEntity.noContent().build();
    }
}
