package org.ust.project.controller;

import org.ust.project.dto.InventoryItemRequestDTO;
import org.ust.project.dto.InventoryItemResponseDTO;
import org.ust.project.service.InventoryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory-items")
public class InventoryItemController {

    @Autowired
    private InventoryItemService inventoryItemService;

    // Create a new inventory item
    @PostMapping
    public ResponseEntity<InventoryItemResponseDTO> createInventoryItem(@RequestBody InventoryItemRequestDTO inventoryItemRequestDTO) {
        InventoryItemResponseDTO inventoryItemResponseDTO = inventoryItemService.createInventoryItem(inventoryItemRequestDTO);
        return new ResponseEntity<>(inventoryItemResponseDTO, HttpStatus.CREATED);
    }

    // Get inventory item by ID
    @GetMapping("/{id}")
    public ResponseEntity<InventoryItemResponseDTO> getInventoryItemById(@PathVariable Long id) {
        InventoryItemResponseDTO inventoryItemResponseDTO = inventoryItemService.getInventoryItemById(id);
        if (inventoryItemResponseDTO != null) {
            return new ResponseEntity<>(inventoryItemResponseDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Or handle not found case
    }

    // Get all inventory items
    @GetMapping
    public ResponseEntity<List<InventoryItemResponseDTO>> getAllInventoryItems() {
        List<InventoryItemResponseDTO> inventoryItemList = inventoryItemService.getAllInventoryItems();
        return new ResponseEntity<>(inventoryItemList, HttpStatus.OK);
    }

    // Update inventory item details
    @PutMapping("/{id}")
    public ResponseEntity<InventoryItemResponseDTO> updateInventoryItem(@PathVariable Long id, @RequestBody InventoryItemRequestDTO inventoryItemRequestDTO) {
        InventoryItemResponseDTO updatedInventoryItem = inventoryItemService.updateInventoryItem(id, inventoryItemRequestDTO);
        if (updatedInventoryItem != null) {
            return new ResponseEntity<>(updatedInventoryItem, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Or handle not found case
    }

    // Delete inventory item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventoryItem(@PathVariable Long id) {
        if (inventoryItemService.deleteInventoryItem(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Successfully deleted
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Item not found
    }
}
