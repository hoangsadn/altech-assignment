package com.altech.electronicstore.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertBasketRequest {
    private Long productId;
    private int quantity;

}
