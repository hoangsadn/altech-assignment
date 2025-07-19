package com.altech.electronicstore.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketDTO {
    @NonNull
    private Long id;
    @NonNull
    private Long userId;
    @NonNull
    private Long version;
    private List<ProductDTO> products = List.of();
}

