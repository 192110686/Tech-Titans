package org.ust.project.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PrescriptionRequestDTO {

    private String medicationName;

    private Double dosageMg;

    private Double frequency;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long consultationId;

    private List<InventoryItemIdQuantityDTO> inventoryItems;

}
