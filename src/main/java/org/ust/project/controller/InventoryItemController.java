package org.ust.project.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.ust.project.dto.InventoryItemRequestDTO;
import org.ust.project.dto.InventoryItemResponseDTO;
import org.ust.project.service.InventoryItemService;

@RestController
@RequestMapping("/inventory-items")
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;

    public InventoryItemController(InventoryItemService inventoryItemService) {
        this.inventoryItemService = inventoryItemService;
    }

    /* ================= CREATE ================= */
    @PostMapping
    public ResponseEntity<InventoryItemResponseDTO> createInventoryItem(
            @Valid @RequestBody InventoryItemRequestDTO requestDTO) {

        InventoryItemResponseDTO response =
                inventoryItemService.createInventoryItem(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /* ================= GET BY ID ================= */
    @GetMapping("/{id}")
    public ResponseEntity<InventoryItemResponseDTO> getInventoryItemById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                inventoryItemService.getInventoryItemById(id)
        );
    }

    /* ================= GET ALL ================= */
    @GetMapping
    public ResponseEntity<List<InventoryItemResponseDTO>> getAllInventoryItems() {

        return ResponseEntity.ok(
                inventoryItemService.getAllInventoryItems()
        );
    }

    /* ================= UPDATE ================= */
    @PutMapping("/{id}")
    public ResponseEntity<InventoryItemResponseDTO> updateInventoryItem(
            @PathVariable Long id,
            @Valid @RequestBody InventoryItemRequestDTO requestDTO) {

        return ResponseEntity.ok(
                inventoryItemService.updateInventoryItem(id, requestDTO)
        );
    }

    /* ================= DELETE ================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventoryItem(@PathVariable Long id) {

        inventoryItemService.deleteInventoryItem(id);
        return ResponseEntity.noContent().build();
    }
}
