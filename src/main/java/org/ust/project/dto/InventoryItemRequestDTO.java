package org.ust.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemRequestDTO {

    private String itemName;

    private String category;

    private Double unitPrice;

    private Double quantity;

    private String supplier;

    private String description;
}
