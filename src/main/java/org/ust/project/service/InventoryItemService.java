package org.ust.project.service;

import org.ust.project.dto.InventoryItemRequestDTO;
import org.ust.project.dto.InventoryItemResponseDTO;
import org.ust.project.model.InventoryItem;
import org.ust.project.repo.InventoryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryItemService {

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    // Create a new inventory item
    public InventoryItemResponseDTO createInventoryItem(InventoryItemRequestDTO inventoryItemRequestDTO) {
        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setItemName(inventoryItemRequestDTO.getItemName());
        inventoryItem.setCategory(inventoryItemRequestDTO.getCategory());
        inventoryItem.setUnitPrice(inventoryItemRequestDTO.getUnitPrice());
        inventoryItem.setQuantity(inventoryItemRequestDTO.getQuantity());
        inventoryItem.setSupplier(inventoryItemRequestDTO.getSupplier());
        inventoryItem.setDescription(inventoryItemRequestDTO.getDescription());

        inventoryItem = inventoryItemRepository.save(inventoryItem);

        return new InventoryItemResponseDTO(
                inventoryItem.getId(),
                inventoryItem.getItemName(),
                inventoryItem.getCategory(),
                inventoryItem.getUnitPrice()
        );
    }

    // Get inventory item by ID
    public InventoryItemResponseDTO getInventoryItemById(Long id) {
        Optional<InventoryItem> inventoryItemOptional = inventoryItemRepository.findById(id);
        if (inventoryItemOptional.isPresent()) {
            InventoryItem inventoryItem = inventoryItemOptional.get();
            return new InventoryItemResponseDTO(
                    inventoryItem.getId(),
                    inventoryItem.getItemName(),
                    inventoryItem.getCategory(),
                    inventoryItem.getUnitPrice()
            );
        }
        return null; // Or throw an exception if you prefer
    }

    // Get all inventory items
    public List<InventoryItemResponseDTO> getAllInventoryItems() {
        List<InventoryItem> inventoryItems = inventoryItemRepository.findAll();
        return inventoryItems.stream()
                .map(inventoryItem -> new InventoryItemResponseDTO(
                        inventoryItem.getId(),
                        inventoryItem.getItemName(),
                        inventoryItem.getCategory(),
                        inventoryItem.getUnitPrice()
                ))
                .collect(Collectors.toList());
    }

    // Update inventory item details
    public InventoryItemResponseDTO updateInventoryItem(Long id, InventoryItemRequestDTO inventoryItemRequestDTO) {
        Optional<InventoryItem> inventoryItemOptional = inventoryItemRepository.findById(id);
        if (inventoryItemOptional.isPresent()) {
            InventoryItem inventoryItem = inventoryItemOptional.get();
            inventoryItem.setItemName(inventoryItemRequestDTO.getItemName());
            inventoryItem.setCategory(inventoryItemRequestDTO.getCategory());
            inventoryItem.setUnitPrice(inventoryItemRequestDTO.getUnitPrice());
            inventoryItem.setQuantity(inventoryItemRequestDTO.getQuantity());
            inventoryItem.setSupplier(inventoryItemRequestDTO.getSupplier());
            inventoryItem.setDescription(inventoryItemRequestDTO.getDescription());

            inventoryItem = inventoryItemRepository.save(inventoryItem);

            return new InventoryItemResponseDTO(
                    inventoryItem.getId(),
                    inventoryItem.getItemName(),
                    inventoryItem.getCategory(),
                    inventoryItem.getUnitPrice()
            );
        }
        return null; // Or throw an exception if item not found
    }

    // Delete inventory item
    public boolean deleteInventoryItem(Long id) {
        if (inventoryItemRepository.existsById(id)) {
            inventoryItemRepository.deleteById(id);
            return true;
        }
        return false; // Or throw an exception if item not found
    }
}
