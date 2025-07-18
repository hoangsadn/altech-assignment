package com.altech.electronicstore.assignment.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptItem {
    private String productName;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
}
