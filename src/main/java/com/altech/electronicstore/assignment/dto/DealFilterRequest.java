package com.altech.electronicstore.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DealFilterRequest {
    private Integer page;
    private Integer size;
    private String sortBy = "name";
    private String sortDirection = ProductFilterRequest.DIC_SORT.ASC.name(); // "asc" or "desc"
    private String searchQuery;
    private Boolean active;

}
