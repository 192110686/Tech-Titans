package org.ust.project.dto;

import lombok.Data;

@Data
public class InventoryItemResponseDTO {

    private Long id;

    private String itemName;

    private String category;

    private Double unitPrice;

    private Double quantity;

    private String supplier;

    private String description;
}
