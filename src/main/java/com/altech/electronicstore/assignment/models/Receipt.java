package com.altech.electronicstore.assignment.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {
    private List<ReceiptItem> items;
    private List<String> dealsApplied;
    private double totalPrice;
}
