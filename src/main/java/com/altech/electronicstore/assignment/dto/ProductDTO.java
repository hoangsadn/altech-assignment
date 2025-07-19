package com.altech.electronicstore.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    @NonNull
    private Long id;

    @NonNull
    private Long version;
    private String name;
    private String description;
    private String imageUrl;
    private int stock;
    private double price;
    private String category;
}

