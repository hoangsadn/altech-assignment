package com.altech.electronicstore.assignment.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Version;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Version
    private Long version; // Optimistic locking field

    private String name;
    private String description;
    private String imageUrl;
    private int stock;
    private double price;
    private String category;

    @ManyToMany(mappedBy = "products")
    @JsonIgnore
    private List<Basket> baskets = new ArrayList<>();
}
