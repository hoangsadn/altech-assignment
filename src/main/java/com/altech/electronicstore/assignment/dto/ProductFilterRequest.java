package com.altech.electronicstore.assignment.dto;

import lombok.Data;

@Data
public class ProductFilterRequest {
    private String category;
    private Double minPrice;
    private Double maxPrice;
    private Boolean available;
    private Integer page;
    private Integer size;
    private String sortBy = "name";
    private String sortDirection = DIC_SORT.ASC.name(); // "asc" or "desc"
    private String searchQuery = "";


    public enum DIC_SORT{
        ASC("asc"),
        DESC("desc");

        DIC_SORT(String direction) {
        }
    }
}
