package com.altech.electronicstore.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ReceiptItem {
    private String productName;
    private int quantity;
    private double unitPrice;

    private double totalPrice;
    private double discountedPrice = 0.0; // Default to 0.0 if not provided
}
